package com.example.baselibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.gsls.gt.GT;
import com.gsls.gt_databinding.route.GT_RouteMeta;
import com.gsls.gt_databinding.route.annotation.GT_ARouterName;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

//路由
public class ARouter {

    private static boolean isDebugARouter = false;//ARouter调试开关
    private volatile static ARouter instance = null;//路由
    private static final Map<String, GT_RouteMeta> aRouterMap = new ArrayMap<>();//路由容器
    private static final Map<String, Object> aRouterStickyMap = new ArrayMap<>();//临时粘性参数事件
    private static SoftReference<Context> softReference;//内部引用 Application
    private static SoftReference<Activity> softReferenceActivity;//内部引用 activity
    private static SoftReference<GT.ARouter.InterceptorCallback> softReferenceNavigationCallback;//内部引用 NavigationCallback
    private ARouterBean aRouterBean;

    private class ARouterBean {

        public static final int INDEX_DEFAULT = -1079;//默认Int值
        private String path;//目标路径
        private String[] claIntercepts;//绿色通道

        public void setClaIntercepts(String[] claIntercepts) {
            this.claIntercepts = claIntercepts;
        }

        public String[] getClaIntercepts() {
            return claIntercepts;
        }

        private int flags = INDEX_DEFAULT;//activity的启动模式
        //启动动画
        private int enterAnim = INDEX_DEFAULT, exitAnim = INDEX_DEFAULT, enterBackAnim = INDEX_DEFAULT, exitBackAnim = INDEX_DEFAULT;
        private int[] anim;
        private Map<String, Object> paramsType;  // 注册参数
        private Map<String, Object> bundleParams;  //bundle 参数

        public ARouterBean(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getFlags() {
            return flags;
        }

        public void setFlags(int flags) {
            this.flags = flags;
        }

        public Map<String, Object> getBundleParams() {
            return bundleParams;
        }

        public void addBundleParams(String key, Object value) {
            if (bundleParams == null) bundleParams = new HashMap<>();
            bundleParams.put(key, value);//每次都只保存最新的那个
        }

        public Map<String, Object> getParamsType() {
            return paramsType;
        }

        public void addParamsType(String key, Object value) {
            if (paramsType == null) paramsType = new HashMap<>();
            paramsType.put(key, value);//每次都只保存最新的那个
        }
    }

    public static ARouter getInstance() {
        if (instance == null) {
            synchronized (ARouter.class) {
                if (instance == null) {
                    instance = new ARouter();
                }
            }
        }
        return instance;
    }

    /**
     * 注册 获取注解
     *
     * @param object
     * @return
     */
    public ARouter inject(Object object, String... gtRoutePaths) {
        Context context = softReference.get();
        //路由检查是否自动依赖注入值
        Field[] fields = object.getClass().getDeclaredFields();//获致当前 Activity 所有成员变更
        for (Field field : fields) {
            GT_Autowired annotation = field.getAnnotation(GT_Autowired.class);
            if (annotation == null) continue;
            String name = annotation.value();
            //如果 注解里的key为空，那就直接用当前变量名作为key
            if (name == null || name.isEmpty()) {
                name = field.getName();
            }

            Object value = null;
            try {

                //获取当前注解类路由路径 作为依赖注解参数的 前缀 key
                String gtRoutePath = "";
                boolean isRemove = true;//是否注解参数自动释放
                if (gtRoutePaths != null && gtRoutePaths.length != 0 && gtRoutePaths[0] != null) {
                    gtRoutePath = gtRoutePaths[0];
                    isRemove = false;
                }

                if (gtRoutePath.isEmpty()) {
                    GT_Route gtRoute = object.getClass().getAnnotation(GT_Route.class);
                    if (gtRoute != null) {
                        gtRoutePath = gtRoute.value();
                    }
                }

                //要保证，主动传参优先级大于一切架构赋值
                if (aRouterStickyMap.containsKey(gtRoutePath + name)) {
                    value = aRouterStickyMap.get(gtRoutePath + name);
                    if (isRemove) {
                        aRouterStickyMap.remove(gtRoutePath + name);
                    }
                } else if (aRouterMap.containsKey(name)) {//判断是否依赖 路由路径来自动注入
                    GT_RouteMeta gtRouteMeta = aRouterMap.get(name);
                    if (gtRouteMeta != null && gtRouteMeta.getDestination() != null) {
                        value = GT.AnnotationAssist.classToObject(gtRouteMeta.getDestination());
                    }
                } else {//如果 既没有主动传值 也没有 依赖路由路径，那就自动生成默认对象
                    if (GT.ARouter.IProvider.class.isAssignableFrom(field.getType())) {
                        for (String key : aRouterMap.keySet()) {
                            GT_RouteMeta gtRouteMeta = aRouterMap.get(key);
                            if (field.getType().isAssignableFrom(gtRouteMeta.getDestination())) {
                                value = GT.AnnotationAssist.classToObject(gtRouteMeta.getDestination());
                                GT.AnnotationAssist.setReflectMethodValue(value, "init", null, Context.class, context);
                                break;
                            }
                        }
                    }
                }

                //给注解类进行依赖注入
                if (value != null) {
                    GT.AnnotationAssist.setReflectVariateValue(object, field.getName(), value);
                }
            } catch (Exception e) {
                if (GT.LOG.GT_LOG_TF) {
                    GT.errs("类型有不匹配的 e:" + e);
                }
            }

        }
        return this;
    }

    /**
     * 手动释放依赖注解资源
     *
     * @param object
     * @return
     */
    public ARouter unregister(Object object) {
        //路由检查是否自动依赖注入值
        Field[] fields = object.getClass().getDeclaredFields();//获致当前 Activity 所有成员变更
        for (Field field : fields) {
            GT_Autowired annotation = field.getAnnotation(GT_Autowired.class);
            if (annotation == null) continue;
            String name = annotation.value();
            //如果 注解里的key为空，那就直接用当前变量名作为key
            if (name == null || name.isEmpty()) {
                name = field.getName();
            }
            try {
                //获取当前注解类路由路径 作为依赖注解参数的 前缀 key
                String gtRoutePath = "";
                GT_Route gtRoute = object.getClass().getAnnotation(GT_Route.class);
                if (gtRoute != null) {
                    gtRoutePath = gtRoute.value();
                }
                aRouterStickyMap.remove(gtRoutePath + name);
            } catch (Exception e) {
                if (GT.LOG.GT_LOG_TF) {
                    GT.errs("类型有不匹配的 e:" + e);
                }
            }

        }
        return this;
    }


    /**
     * 销毁路由所有资源
     *
     * @return
     */
    public ARouter destroy() {
        clear();
        if (softReference != null) softReference.clear();
        if (aRouterMap != null) aRouterMap.clear();
        instance = null;
        return this;
    }

    /**
     * 清空路由 可清空缓存数据
     * 该方法请谨慎使用，会将路由传参也清理掉，
     * 若你确定没有传参缓存，即可使用
     *
     * @return
     */
    public ARouter clear() {
        aRouterBean = null;//2
        if (aRouterStickyMap != null) aRouterStickyMap.clear();
        if (softReferenceActivity != null) softReferenceActivity.clear();
        if (softReferenceNavigationCallback != null) softReferenceNavigationCallback.clear();
        return this;
    }

    /**
     * 构建跳转路由目的
     *
     * @param path
     * @return
     */
    public ARouter build(String path) {
        aRouterBean = new ARouterBean(path);
        return this;
    }

    /**
     * 动态设置 跳转Activity的Flags
     *
     * @param flags 参数值
     * @return
     */
    public ARouter withFlags(int flags) {
        if (aRouterBean == null) return this;
        aRouterBean.setFlags(flags);
        return this;
    }

    /**
     * 注解传递参数
     *
     * @param key   参数名称
     * @param value 参数值
     * @return
     */
    public ARouter withObject(String key, Object value) {
        if (aRouterBean == null) return this;
        //需要完成，注解赋值
        aRouterBean.addParamsType(aRouterBean.getPath() + key, value);
        return this;
    }

    /**
     * 通过 Bundle 传递参数
     *
     * @param key   参数key
     * @param value 参数值
     * @return
     */
    public ARouter putExtra(String key, Object value) {
        if (aRouterBean == null) return this;
        aRouterBean.addBundleParams(key, value);
        return this;
    }

    /**
     * 使用转场动画
     *
     * @param anim [0][1] = 启动动画 、[2][3] = 返回动画
     * @return
     */
    public ARouter withTransition(int... anim) {
        if (aRouterBean == null || anim == null || anim.length <= 1) return this;
        aRouterBean.anim = anim;
        switch (anim.length) {
            case 2:
                aRouterBean.enterAnim = anim[0];
                aRouterBean.exitAnim = anim[1];
                break;
            case 3:
                aRouterBean.enterAnim = anim[0];
                aRouterBean.exitAnim = anim[1];
                aRouterBean.enterBackAnim = anim[2];
                aRouterBean.exitBackAnim = anim[1];
                break;
            case 4:
                aRouterBean.enterAnim = anim[0];
                aRouterBean.exitAnim = anim[1];
                aRouterBean.enterBackAnim = anim[2];
                aRouterBean.exitBackAnim = anim[3];
                break;
        }

        return this;
    }

    /**
     * 使用转场动画
     *
     * @param enterAnim
     * @param exitAnim
     * @return
     */
    public ARouter withTransition(int enterAnim, int exitAnim) {
        if (aRouterBean == null) return this;
        aRouterBean.enterAnim = enterAnim;
        aRouterBean.exitAnim = exitAnim;
        return this;
    }

    /**
     * 使用绿色通道，默认跳过所有拦截器，可指定跳过的拦截器
     *
     * @param claIntercepts
     * @return
     */
    public ARouter greenChannal(String... claIntercepts) {
        if (aRouterBean == null) return this;
        aRouterBean.claIntercepts = claIntercepts;
        return this;
    }

    /**
     * @param cla 指定返回类型q
     * @param <T>
     * @return
     */
    public <T> T navigation(Class<T> cla) {
        //如果是需要通过 cla 依赖注入
        if (aRouterBean == null || aRouterBean.path == null) {
            if (GT.ARouter.IProvider.class.isAssignableFrom(cla)) {
                for (String key : aRouterMap.keySet()) {
                    GT_RouteMeta gtRouteMeta = aRouterMap.get(key);
                    if (gtRouteMeta == null) continue;
                    if (cla.isAssignableFrom(gtRouteMeta.getDestination())) {
                        T value = (T) GT.AnnotationAssist.classToObject(gtRouteMeta.getDestination());
                        Context context = softReference.get();
                        if (context != null) {
                            GT.AnnotationAssist.setReflectMethodValue(value, "init", null, Context.class, context);
                        }
                        return value;
                    }
                }
            }
        }
        //否则就执行 路由基本逻辑
        return navigation();
    }

    /**
     * 启动拦截器
     *
     * @param interceptorCallback 拦截器监听
     * @param <T>
     * @return
     */
    public <T> T navigation(GT.ARouter.InterceptorCallback interceptorCallback) {
        softReferenceNavigationCallback = new SoftReference(interceptorCallback);
        //否则就执行 路由基本逻辑
        return navigation();
    }

    /**
     * 拦截器方式
     *
     * @param activity            上下文
     * @param interceptorCallback 拦截器监听
     * @param <T>
     * @return
     */
    public <T> T navigation(Activity activity, GT.ARouter.InterceptorCallback... interceptorCallback) {
        softReferenceActivity = new SoftReference(activity);
        if (interceptorCallback != null && interceptorCallback.length > 0 && interceptorCallback[0] != null) {
            softReferenceNavigationCallback = new SoftReference(interceptorCallback[0]);
        }
        //否则就执行 路由基本逻辑
        return navigation();
    }

    public <T> T navigation(Activity activity) {
        softReferenceActivity = new SoftReference(activity);
        //否则就执行 路由基本逻辑
        return navigation();
    }


    public <T> T navigation() {
        if (aRouterBean == null) return null;
        GT_RouteMeta gt_routeMeta = aRouterMap.get(aRouterBean.path);
        if (gt_routeMeta == null) return null;
        Context context = null;

        //先拿Activity
        if (softReferenceActivity != null) {
            context = softReferenceActivity.get();
        }

        //若没有就直接拿 Application
        if (softReference != null && context == null) {
            context = softReference.get();
        }

        try {
            //如果使用注解传递，那就进行 监听者消息 方法传递
            Map<String, Object> paramsType = aRouterBean.getParamsType();
            if (paramsType != null && !paramsType.keySet().isEmpty()) {
                for (String key : paramsType.keySet()) {
                    Object value = paramsType.get(key);
                    aRouterStickyMap.put(key, value);
                }
            }

            boolean isAbort;//默认不拦截
            switch (gt_routeMeta.getType()) {
                case ACTIVITY, VIEW,
                        FRAGMENT, FRAGMENT_X,
                        DIALOG_FRAGMENT, DIALOG_FRAGMENT_X,
                        BASE_VIEW, FLOATING_WINDOW, POPUP_WINDOW, NOTIFICATION, PROVIDER://需要添加拦截器的
                    isAbort = loadInterceptor(context, gt_routeMeta);//加载拦截器
                    if (!isAbort) {
                        switch (gt_routeMeta.getType()) {
                            case ACTIVITY:
                                setActivity(context, gt_routeMeta);//处理 路由逻辑
                                break;
                            case VIEW:
                                View view;
                                view = setView(gt_routeMeta);//处理 路由逻辑
                                if (view != null) {
                                    aRouterBean = null;
                                    return (T) view;
                                }
                                break;
                            case FRAGMENT, FRAGMENT_X:
                                Fragment fragment;
                                fragment = setFragment(gt_routeMeta);//处理 路由逻辑
                                if (fragment != null) {
                                    aRouterBean = null;
                                    return (T) fragment;
                                }
                            case DIALOG_FRAGMENT, DIALOG_FRAGMENT_X:
                                DialogFragment dialogFragment;
                                dialogFragment = setDialogFragment(gt_routeMeta);//处理 路由逻辑
                                if (dialogFragment != null) {
                                    aRouterBean = null;
                                    return (T) dialogFragment;
                                }
                            case BASE_VIEW:
                                GT.GT_View.BaseView baseView;
                                baseView = setBaseView(gt_routeMeta);//处理 路由逻辑
                                if (baseView != null) {
                                    aRouterBean = null;
                                    return (T) baseView;
                                }
                            case FLOATING_WINDOW://悬浮窗
                                GT.GT_FloatingWindow.BaseFloatingWindow floatingWindow;
                                floatingWindow = setFloatingWindow(gt_routeMeta);//处理 路由逻辑
                                if (floatingWindow != null) {
                                    aRouterBean = null;
                                    return (T) floatingWindow;
                                }
                            case POPUP_WINDOW://悬浮窗
                                GT.GT_PopupWindow.BasePopupWindow popupWindow;
                                popupWindow = setPopupWindow(gt_routeMeta);//处理 路由逻辑
                                if (popupWindow != null) {
                                    aRouterBean = null;
                                    return (T) popupWindow;
                                }

                            case NOTIFICATION://悬浮窗
                                GT.GT_Notification.BaseNotification notification;
                                notification = setNotification(gt_routeMeta);//处理 路由逻辑
                                if (notification != null) {
                                    aRouterBean = null;
                                    return (T) notification;
                                }
                            case PROVIDER://接口方法、传值、传参
                                Object obj;
                                obj = setProvider(context, gt_routeMeta);//处理 接口方法 路由逻辑
                                if (obj != null) {
                                    aRouterBean = null;
                                    return (T) obj;
                                }
                        }
                    }
                    aRouterBean = null;//1
                    return null;
                case INTERCEPTOR://直接跳转拦截器
                    startInterceptor(context, gt_routeMeta);//加载拦截器
                    aRouterBean = null;//1
                    return null;
                //待实现
                case SERVICE:
                    break;
                case CONTENT_PROVIDER:
                    break;
                case UNKNOWN:
                    break;
            }
        } catch (Exception e) {
            if (ARouter.isDebugARouter) {
                GT.err(" e:" + e);
            }
        }
        aRouterBean = null;//9
        return (T) gt_routeMeta;
    }

    @SuppressLint("WrongConstant")
    private void setActivity(Context context, GT_RouteMeta gt_routeMeta) {
        if (context != null) {
            Intent intent = new Intent(context, gt_routeMeta.getDestination());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (aRouterBean.flags != ARouterBean.INDEX_DEFAULT) {
                intent.setFlags(aRouterBean.flags);
            }
            setBundleParams(intent);

            //设置Activity 动态转场
            if (aRouterBean.enterBackAnim != ARouterBean.INDEX_DEFAULT && aRouterBean.exitBackAnim != ARouterBean.INDEX_DEFAULT) {
                //传递参数
                intent.putExtra(gt_routeMeta.getDestination() + "enterBackAnim", aRouterBean.enterBackAnim);
                intent.putExtra(gt_routeMeta.getDestination() + "exitBackAnim", aRouterBean.exitBackAnim);
            }

            //动态添加 转场
            if (aRouterBean.enterAnim != ARouterBean.INDEX_DEFAULT && aRouterBean.exitAnim != ARouterBean.INDEX_DEFAULT) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(context, aRouterBean.enterAnim, aRouterBean.exitAnim);
                try {
                    context.startActivity(intent, options.toBundle());
                } catch (Exception e) {
                    context.startActivity(intent);
                }
            } else {
                context.startActivity(intent);
            }
        }
    }

    private DialogFragment setDialogFragment(GT_RouteMeta gt_routeMeta) {
        DialogFragment dialogFragment = null;
        try {
            Object obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath());
            if (obj instanceof DialogFragment) {
                dialogFragment = (DialogFragment) obj;
                Bundle bundle = new Bundle();
                setFragmentParams(bundle);
                dialogFragment.setArguments(bundle);
            }
        } catch (Exception e) {
        }
        return dialogFragment;
    }

    private Fragment setFragment(GT_RouteMeta gt_routeMeta) {
        Fragment fragment = null;
        try {
            Object obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath());
            if (obj instanceof Fragment) {
                fragment = (Fragment) obj;
                Bundle bundle = new Bundle();
                setFragmentParams(bundle);
                bundle.putIntArray(gt_routeMeta.getDestination() + "anim", aRouterBean.anim);
                fragment.setArguments(bundle);
            }
        } catch (Exception e) {
        }
        return fragment;
    }

    private View setView(GT_RouteMeta gt_routeMeta) {
        try {
            Object obj;
            if (aRouterBean != null && aRouterBean.getBundleParams() != null && !aRouterBean.getBundleParams().isEmpty()) {//是否传递参数 Bundle
                Bundle bundle = new Bundle();
                setFragmentParams(bundle);
                obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath(), new Class[]{Context.class, Bundle.class}, new Object[]{softReference.get(), bundle});
            } else {//没有传递 Bundle 参数
                obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath(), new Class[]{Context.class}, new Object[]{softReference.get()});
            }
            if (obj instanceof View) {
                return (View) obj;
            }
        } catch (Exception e) {
            GT.errt("e:" + e);
        }
        return null;
    }

    private GT.GT_View.BaseView setBaseView(GT_RouteMeta gt_routeMeta) {
        GT.GT_View.BaseView baseView = null;
        try {
            Object obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath());
            if (obj instanceof GT.GT_View.BaseView) {
                baseView = (GT.GT_View.BaseView) obj;
                Bundle bundle = new Bundle();
                setFragmentParams(bundle);
                baseView.setArguments(bundle);
            }
        } catch (Exception e) {
            GT.errt("e:" + e);
        }
        return baseView;
    }

    private GT.GT_FloatingWindow.BaseFloatingWindow setFloatingWindow(GT_RouteMeta gt_routeMeta) {
        GT.GT_FloatingWindow.BaseFloatingWindow floatingWindow = null;
        try {
            Object obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath());
            if (obj instanceof GT.GT_FloatingWindow.BaseFloatingWindow) {
                floatingWindow = (GT.GT_FloatingWindow.BaseFloatingWindow) obj;
                Bundle bundle = new Bundle();
                setFragmentParams(bundle);
                floatingWindow.setArguments(bundle);
            }
        } catch (Exception e) {
            GT.errt("e:" + e);
        }
        return floatingWindow;

    }

    private GT.GT_PopupWindow.BasePopupWindow setPopupWindow(GT_RouteMeta gt_routeMeta) {
        GT.GT_PopupWindow.BasePopupWindow popupWindow = null;
        try {
            Object obj;
            if (aRouterBean != null && aRouterBean.getBundleParams() != null && !aRouterBean.getBundleParams().isEmpty()) {//是否传递参数 Bundle
                Bundle bundle = new Bundle();
                setFragmentParams(bundle);
                obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath(), new Class[]{Context.class, Bundle.class}, new Object[]{softReference.get(), bundle});
            } else {//没有传递 Bundle 参数
                obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath(), new Class[]{Context.class}, new Object[]{softReference.get()});
            }
            if (obj instanceof GT.GT_PopupWindow.BasePopupWindow) {
                popupWindow = (GT.GT_PopupWindow.BasePopupWindow) obj;
            }
        } catch (Exception e) {
            GT.errt("e:" + e);
        }
        return popupWindow;

    }

    private GT.GT_Notification.BaseNotification setNotification(GT_RouteMeta gt_routeMeta) {
        GT.GT_Notification.BaseNotification notification  = null;
        try {
            Object obj;
            if (aRouterBean != null && aRouterBean.getBundleParams() != null && !aRouterBean.getBundleParams().isEmpty()) {//是否传递参数 Bundle
                Bundle bundle = new Bundle();
                setFragmentParams(bundle);
                obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath(), new Class[]{Context.class, Bundle.class}, new Object[]{softReference.get(), bundle});
            } else {//没有传递 Bundle 参数
                obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getPackClassPath(), new Class[]{Context.class}, new Object[]{softReference.get()});
            }
            if (obj instanceof GT.GT_Notification.BaseNotification) {
                notification = (GT.GT_Notification.BaseNotification) obj;
            }
        } catch (Exception e) {
            GT.errt("e:" + e);
        }
        return notification;

    }

    private Object setProvider(Context context, GT_RouteMeta gt_routeMeta) {
        Object obj = null;
        try {
            obj = GT.AnnotationAssist.classToObject(gt_routeMeta.getDestination());
            GT.AnnotationAssist.setReflectMethodValue(obj, "init", null, Context.class, context);
        } catch (Exception e) {
        }
        return obj;
    }

    /**
     * 启动拦截器
     *
     * @param context
     * @param gt_routeMeta
     * @return
     */
    private boolean startInterceptor(Context context, GT_RouteMeta gt_routeMeta) {
        //本次跳转 是否存在拦截
        String[] interceptors = gt_routeMeta.getInterceptors();
        if (interceptors == null || interceptors.length == 0) return false;//如果没有设置拦截器，那就返回不拦截

        //本次拦截 是否存在监听
        GT.ARouter.InterceptorCallback interceptorCallback = null;
        if (softReferenceNavigationCallback != null) {
            interceptorCallback = softReferenceNavigationCallback.get();
        }

        //默认不拦截
        final boolean[] isAbort = {false};

        //传递参数
        Intent intent = null;
        if (context instanceof Activity activity) {
            intent = activity.getIntent();
        }

        Intent intentBundleParam = setBundleParams(intent);
        if (intent == null) {
            intent = intentBundleParam;
        }

        //按被调用的拦截器遍历顺序处理
        for (int i = 0; i < interceptors.length; i++) {
            if (isAbort[0]) break;//如果上个拦截器已拦截，那就直接停止遍历拦截
            String interceptor = interceptors[i];
            Object obj;
            GT_RouteMeta gtRouteMeta = aRouterMap.get(interceptor);//获取这个拦截器的信息
            try {
                //实例化拦截器
                obj = GT.AnnotationAssist.classToObject(gtRouteMeta.getDestination());

                //调用初始化
                GT.AnnotationAssist.setReflectMethodValue(obj, "init", null, new Class[]{Context.class, String.class}, context, "");

                GT.ARouter.InterceptorCallback finalInterceptorCallback = interceptorCallback;
                //进行拦截返回
                int finalI = i;
                GT.AnnotationAssist.setReflectMethodValue(obj, "process", Boolean.class,
                        new Class[]{Intent.class, GT.ARouter.InterceptorCallback.class}, intent, new GT.ARouter.InterceptorCallback() {
                            @Override
                            public boolean onContinue(Intent intent) {
                                if (finalI == interceptors.length - 1 && finalInterceptorCallback != null) {
                                    finalInterceptorCallback.onContinue(intent);
                                }
                                if (softReferenceNavigationCallback != null)
                                    softReferenceNavigationCallback.clear();
                                return super.onContinue(intent);
                            }

                            @Override
                            public boolean onAbort(Intent intent) {
                                isAbort[0] = true;
                                if (finalInterceptorCallback != null) {
                                    finalInterceptorCallback.onAbort(intent);
                                }
                                if (softReferenceNavigationCallback != null)
                                    softReferenceNavigationCallback.clear();
                                return super.onAbort(intent);
                            }
                        });


            } catch (Exception e) {
            }

            if (softReferenceActivity != null) softReferenceActivity.clear();
            //遍历拦截循环结束
        }

        return isAbort[0];
    }

    /**
     * 加载拦截器
     *
     * @param context
     * @param gt_routeMeta
     * @return
     */
    private boolean loadInterceptor(Context context, GT_RouteMeta gt_routeMeta) {
        //本次跳转 是否存在拦截
        String[] interceptors = gt_routeMeta.getInterceptors();
        if (interceptors == null || interceptors.length == 0) return false;//如果没有设置拦截器，那就返回不拦截

        //本次拦截 是否存在监听
        GT.ARouter.InterceptorCallback interceptorCallback = null;
        if (softReferenceNavigationCallback != null) {
            interceptorCallback = softReferenceNavigationCallback.get();
        }

        //默认不拦截
        final boolean[] isAbort = {false};

        //传递参数
        Intent intent = null;
        if (context instanceof Activity activity) {
            intent = activity.getIntent();
        }

        Intent intentBundleParam = setBundleParams(intent);
        if (intent == null) {
            intent = intentBundleParam;
        }

        List<String> claInterceptsList = null;
        if (aRouterBean != null) {
            String[] claIntercepts = aRouterBean.claIntercepts;
            if (claIntercepts != null) {
                claInterceptsList = new ArrayList<>(Arrays.asList(claIntercepts));
            }
        }

        //按被调用的拦截器遍历顺序处理
        for (int i = 0; i < interceptors.length; i++) {
            if (isAbort[0]) break;//如果上个拦截器已拦截，那就直接停止遍历拦截
            String interceptor = interceptors[i];
            if (claInterceptsList != null && interceptor != null) {
                if (claInterceptsList.isEmpty()) {
                    if (interceptorCallback != null) interceptorCallback.onContinue(intent);//返回通过拦截
                    break;//全绿色通道
                } else if (claInterceptsList.contains(interceptor)) continue;//跳过绿色通道拦截器
            }
            Object obj;
            GT_RouteMeta gtRouteMeta = aRouterMap.get(interceptor);//获取这个拦截器的信息
            try {
                //实例化拦截器
                obj = GT.AnnotationAssist.classToObject(gtRouteMeta.getDestination());
                //调用初始化
                GT.AnnotationAssist.setReflectMethodValue(obj, "init", null, new Class[]{Context.class, String.class}, context, gt_routeMeta.getPath());

                GT.ARouter.InterceptorCallback finalInterceptorCallback = interceptorCallback;
                //进行拦截返回
                int finalI = i;
                GT.AnnotationAssist.setReflectMethodValue(obj, "process", Boolean.class,
                        new Class[]{Intent.class, GT.ARouter.InterceptorCallback.class}, intent, new GT.ARouter.InterceptorCallback() {
                            @Override
                            public boolean onContinue(Intent intent) {
                                if (finalI == interceptors.length - 1 && finalInterceptorCallback != null) {
                                    finalInterceptorCallback.onContinue(intent);
                                }
                                if (softReferenceNavigationCallback != null)
                                    softReferenceNavigationCallback.clear();
                                return super.onContinue(intent);
                            }

                            @Override
                            public boolean onAbort(Intent intent) {
                                isAbort[0] = true;
                                if (finalInterceptorCallback != null) {
                                    finalInterceptorCallback.onAbort(intent);
                                }
                                if (softReferenceNavigationCallback != null)
                                    softReferenceNavigationCallback.clear();
                                return super.onAbort(intent);
                            }
                        });


            } catch (Exception e) {
            }

            if (softReferenceActivity != null) softReferenceActivity.clear();
            //遍历拦截循环结束
        }
        return isAbort[0];
    }

    /**
     * Bundle 参数
     *
     * @param intent
     */
    private Intent setBundleParams(Intent intent) {
        if (aRouterBean == null) return intent;
        //如果传递的int是 Bundle 数据，那就通过 ent 传递
        try {
            Map<String, Object> bundleParams = aRouterBean.getBundleParams();
            if (bundleParams != null && !bundleParams.keySet().isEmpty()) {
                if (intent == null) intent = new Intent();
                for (String key : bundleParams.keySet()) {
                    Object object = bundleParams.get(key);
                    if (object instanceof String) {
                        intent.putExtra(key, object.toString());
                    } else if (object instanceof Integer) {
                        intent.putExtra(key, (Integer) object);
                    } else if (object instanceof Long) {
                        intent.putExtra(key, (Long) object);
                    } else if (object instanceof Float) {
                        intent.putExtra(key, (Float) object);
                    } else if (object instanceof Boolean) {
                        intent.putExtra(key, (Boolean) object);
                    } else if (object instanceof Double) {
                        intent.putExtra(key, (Double) object);
                    } else if (object instanceof Short) {
                        intent.putExtra(key, (Short) object);
                    } else if (object instanceof Byte) {
                        intent.putExtra(key, (Byte) object);
                    } else if (object instanceof Parcelable) {
                        intent.putExtra(key, (Parcelable) object);
                    } else if (object instanceof Serializable) {
                        intent.putExtra(key, (Serializable) object);
                    } else if (object instanceof Bundle) {
                        intent.putExtra(key, (Bundle) object);
                    } else if (object instanceof Character) {
                        intent.putExtra(key, (Character) object);
                    } else if (object instanceof String[]) {
                        intent.putExtra(key, (String[]) object);
                    } else if (object instanceof Integer[]) {
                        intent.putExtra(key, (Integer[]) object);
                    } else if (object instanceof Float[]) {
                        intent.putExtra(key, (Float[]) object);
                    } else if (object instanceof Boolean[]) {
                        intent.putExtra(key, (Boolean[]) object);
                    } else if (object instanceof Short[]) {
                        intent.putExtra(key, (Short[]) object);
                    } else if (object instanceof Byte[]) {
                        intent.putExtra(key, (Byte[]) object);
                    } else if (object instanceof Double[]) {
                        intent.putExtra(key, (Double[]) object);
                    } else if (object instanceof Long[]) {
                        intent.putExtra(key, (Long[]) object);
                    } else if (object instanceof Serializable[]) {
                        intent.putExtra(key, (Serializable[]) object);
                    } else if (object instanceof Parcelable[]) {
                        intent.putExtra(key, (Parcelable[]) object);
                    } else if (object instanceof Character[]) {
                        intent.putExtra(key, (Character[]) object);
                    } else {//转入 json 存储
                        intent.putExtra(key, GT.JSON.toJson2(object));
                    }

                }
            }
        } catch (Exception e) {

        }
        return intent;
    }

    /**
     * Fragment 参数
     *
     * @param bundle
     */
    private void setFragmentParams(Bundle bundle) {
        if (aRouterBean == null) return;
        //如果传递的int是 Bundle 数据，那就通过 ent 传递
        try {
            Map<String, Object> bundleParams = aRouterBean.getBundleParams();
            if (bundleParams != null && !bundleParams.keySet().isEmpty()) {
                for (String key : bundleParams.keySet()) {
                    Object object = bundleParams.get(key);
                    if (object instanceof String) {
                        bundle.putString(key, object.toString());
                    } else if (object instanceof Integer) {
                        bundle.putInt(key, (Integer) object);
                    } else if (object instanceof Long) {
                        bundle.putLong(key, (Long) object);
                    } else if (object instanceof Float) {
                        bundle.putFloat(key, (Float) object);
                    } else if (object instanceof Boolean) {
                        bundle.putBoolean(key, (Boolean) object);
                    } else if (object instanceof Double) {
                        bundle.putDouble(key, (Double) object);
                    } else if (object instanceof Short) {
                        bundle.putShort(key, (Short) object);
                    } else if (object instanceof Byte) {
                        bundle.putByte(key, (Byte) object);
                    } else if (object instanceof Parcelable) {
                        bundle.putParcelable(key, (Parcelable) object);
                    } else if (object instanceof Serializable) {
                        bundle.putSerializable(key, (Serializable) object);
                    } else if (object instanceof Bundle) {
                        bundle = (Bundle) object;
                    } else if (object instanceof Character) {
                        bundle.putChar(key, (char) object);
                    } else if (object instanceof String[]) {
                        bundle.putStringArray(key, (String[]) object);
                    } else if (object instanceof Integer[]) {
                        bundle.putIntArray(key, (int[]) object);
                    } else if (object instanceof Float[]) {
                        bundle.putFloatArray(key, (float[]) object);
                    } else if (object instanceof Boolean[]) {
                        bundle.putBooleanArray(key, (boolean[]) object);
                    } else if (object instanceof Short[]) {
                        bundle.putShort(key, (Short) object);
                    } else if (object instanceof Byte[]) {
                        bundle.putByteArray(key, (byte[]) object);
                    } else if (object instanceof Double[]) {
                        bundle.putDoubleArray(key, (double[]) object);
                    } else if (object instanceof Long[]) {
                        bundle.putLongArray(key, (long[]) object);
                    } else if (object instanceof Character[]) {
                        bundle.putCharArray(key, (char[]) object);
                    } else {//转入 json 存储
                        bundle.putString(key, GT.JSON.toJson2(object));
                    }

                }
            }
        } catch (Exception e) {

        }
    }

    public static synchronized void openDebug() {
        isDebugARouter = true;
    }

    public static synchronized void printStackTrace() {

    }

    /**
     * Application 初始化
     *
     * @param context
     */
    public static synchronized void init(Context context) {
        softReference = new SoftReference(context);
        //加载路由注解
        loadHibernateAnnotation(context, context.getPackageName());
    }

    /**
     * 处理 路由初始化数据
     *
     * @param context
     * @param packagePath
     */
    private static void loadHibernateAnnotation(Context context, String packagePath) {
        if (packagePath == null) return;
        try {
            String packageCodePath = context.getPackageCodePath();
            DexFile dexFile = new DexFile(packageCodePath);
            Enumeration<String> enumeration = dexFile.entries();
            while (enumeration.hasMoreElements()) {
                String className = enumeration.nextElement();
                //在当前所有可执行的类里面查找包含有该包名的所有类
                if (className.contains(packagePath)) {
                    Class<?> clazz1 = null;
                    try {
                        clazz1 = Class.forName(className);
                    } catch (ClassNotFoundException ignored) {

                    }
                    if (clazz1 == null) continue;//在扫描到第三模块时会为null
                    //过滤掉未被注解过的类
                    GT_ARouterName aRouterName = clazz1.getAnnotation(GT_ARouterName.class);//获取该类 ContextView 的注解类
                    if (aRouterName != null) {
                        String[] values = aRouterName.value();
                        for (String value : values) {
                            Object object = GT.AnnotationAssist.classToObject(value);
                            if (object == null) continue;
                            Map<String, GT_RouteMeta> map = GT.AnnotationAssist.getReflectMethodValue(object, "loadInto", Map.class);
                            for (String key : map.keySet()) {
                                if (!aRouterMap.containsKey(key)) {
                                    aRouterMap.put(key, map.get(key));
                                }
                            }

                        }
                    }

                }
            }

        } catch (Exception e) {
            if (isDebugARouter) {
                GT.errt("e:" + e);
            }
        }

    }

}
