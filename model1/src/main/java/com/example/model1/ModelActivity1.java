package com.example.model1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baselibrary.BaseService;
import com.example.baselibrary.Config;
import com.example.baselibrary.DemoBean;
import com.example.baselibrary.NullViewModelAllL;
import com.example.baselibrary.RoutePath;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.annotation.GT_R_Build;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@GT_R_Build
@GT_Route(value = Config.Model1Config.ModelActivity1.MAIN, extras = "模块1主页")
@GT.Annotations.GT_AnnotationActivity(R2.layout.activity_model1)
public class ModelActivity1 extends GT.GT_Activity.AnnotationActivity {

    @GT_Autowired(value = Config.Model1Config.ModelActivity1.keyList, desc = "这是测试的集合参数")
    private List<Activity> listName;

    @GT_Autowired(Config.Model1Config.ModelActivity1.keyMap)
    private Map<String, Object> mapName;

    @GT_Autowired(Config.Model1Config.ModelActivity1.keyAAA)
    private String name;

    @GT.Annotations.GT_View(R2.id.fl)
    private FrameLayout fl;
    @GT.Annotations.GT_View(R2.id.rv)
    private RecyclerView rv;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
//        GT.logt("初始化 ModelActivity1");
        GT.ARouter.getInstance().inject(this);//与Autowired配合使用
//        GT.logt("nameA:" + name);//只有在 注入路由后才能使用该注解变量
//        GT.logt("listName:" + listName);//只有在 注入路由后才能使用该注解变量
//        GT.logt("mapName:" + mapName);//只有在 注入路由后才能使用该注解变量

        Intent intent = getIntent();
        if (intent != null) {
            int keyBBB = intent.getIntExtra(Config.Model1Config.ModelActivity1.keyBBB, 0);
            boolean keyCCC = intent.getBooleanExtra(Config.Model1Config.ModelActivity1.keyCCC, false);
//            GT.logt("keyBBB:" + keyBBB);
//            GT.logt("keyCCC:" + keyCCC);
        }

        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GT.logt("单击了加载 View 按钮:" + name);
                Bundle bundle = new Bundle();
                bundle.putString("extra", "我的 Bundle extra 参数");

                //TODO 下面的每一步都可以单独解开 运行看效果

                /*GT.GT_WebView.BaseWebView webView = GT.ARouter.getInstance()
                        .build(Config.AppConfig.WEB_VIEW)
                        .constructorParams(
                                new Class[]{Context.class, ViewGroup.class, Bundle.class},
                                new Object[]{ModelActivity1.this, fl, bundle}
                        )
                        .withObject("name", name)
                        .navigation();
                if(webView != null){
                    webView.loadWeb("https://blog.csdn.net/qq_39799899?type=blog");
                }*/


                /*GT.Adapters.BaseAdapter<DemoBean, ?> adapter = GT.ARouter.getInstance()
                        .build(Config.AppConfig.ADAPTER)
                        .constructorParams(
                                new Class[]{Context.class, GT.OnListener.class},
                                new Object[]{ModelActivity1.this, (GT.OnListener<DemoBean>) obj -> {
                                        GT.logt("回调触发了:" + obj[0]);
                                }}
                        )
                        .navigation();
                if (adapter != null) {
                    adapter.setStaggeredGridLayoutManager_V(rv, 3);
                    List<DemoBean> beanList = new ArrayList<>();
                    for (int i = 0; i < 100; i++) {
                        DemoBean demoBean = new DemoBean();
                        demoBean.name = "我的名字:" + i;
                        demoBean.age = i;
                        beanList.add(demoBean);
                    }
                    adapter.setBeanList(beanList);
                }*/


                //路由 方法
                /*BaseService baseService = GT.ARouter.getInstance()
                        .build(Config.AppConfig.LIB1_SERVICE_IMPL_SAY_HELLO)
                        .navigation();
                String s = baseService.sayHello(23);
                GT.logt("方法返回的值:" + s);*/



                /*NullViewModelAllL viewModel = GT.ARouter.getInstance()
                        .build(Config.AppConfig.VIEW_MODEL)
                        .navigation();
                if(viewModel != null){
                    viewModel.getAppData().observe(ModelActivity1.this,
                            new Observer<DemoBean>() {
                        @Override
                        public void onChanged(DemoBean demoBean) {
                            GT.logt("ViewModel:" + demoBean);
                        }
                    });
                }*/


                /*GT.GT_Notification.BaseNotification notification = GT.ARouter.getInstance()
                        .build(Config.AppConfig.NOTIFICATION)
                        .constructorParams(
                                new Class[]{Context.class,  Bundle.class},
                                new Object[]{ModelActivity1.this, bundle}
                        )
                        .withObject("name", name)
                        .navigation(ModelActivity1.this);
                if (notification != null) {
                    notification.commit();
                }*/


                /*GT.GT_PopupWindow.BasePopupWindow popupWindow = GT.ARouter.getInstance()
                        .build(Config.AppConfig.POPUP_WINDOW)
                        .constructorParams(
                                new Class[]{Context.class,  Bundle.class},
                                new Object[]{ModelActivity1.this, bundle}
                        )
                        .withObject("name", name)
                        .navigation(ModelActivity1.this);
                if (popupWindow != null) {
                    popupWindow.startPopupWindowAbsolute(v, -100);
                }*/


                /*GT.GT_FloatingWindow.BaseFloatingWindow floatingWindow = GT.ARouter.getInstance()
                        .build(Config.AppConfig.FLOATING)
                        .putExtra("extra", "我的 extra 参数")
                        .withObject("name", name)
                        .navigation();
                if (floatingWindow != null) {
                    GT.GT_FloatingWindow.BaseFloatingWindow.setType_screenType(
                            GT.GT_FloatingWindow.BaseFloatingWindow.TYPE_DEFAULT);
                    startFloatingWindow(ModelActivity1.this, floatingWindow);
                }*/

                //
               /* View view = GT.ARouter.getInstance()
                                .build(Config.AppConfig.VIEW)
                                .constructorParams(
                                        new Class[]{Context.class,  Bundle.class},
                                        new Object[]{ModelActivity1.this, bundle}
                                )
                                .withObject("name", name)
                                .navigation();
                GT.logt("view:" + view);
                if(view != null){
                    fl.addView(view);
                }*/


                 /*GT.GT_View.BaseView gtView = GT.ARouter.getInstance()
                                .build(Config.AppConfig.BASE_VIEW)
                                .putExtra("extra","我的 extra")
                                .withObject("name", name)
                                .navigation();
                GT.logt("gtView:" + gtView);
                if(gtView != null){
                    gtView.init(ModelActivity1.this, fl);
                }*/


                /*DialogFragment dialogFragment =
                        GT.ARouter.getInstance()
                                .build(Config.AppConfig.DIALOG)
                                .putExtra("extra", "我的 extra")
                                .withObject("name", name)
                                .navigation();
                GT.logt("dialogFragment:" + dialogFragment);
                if(dialogFragment != null){
                    startDialogFragment(dialogFragment);
                }*/



                //直接启动                                                         ，可以直接用拦截器当过滤器
                /*GT.ARouter.getInstance()
                        .build(Config.TestInterceptor.MAIN)
                        .putExtra("extra","我的 extra")
                        .withObject("name", name)
                        .navigation(new GT.ARouter.InterceptorCallback() {
                            @Override
                            public boolean onContinue(Intent intent) {
                                GT.logt("继续执行了");
                                //开始当前其他逻辑
                                return super.onContinue(intent);
                            }

                            @Override
                            public boolean onAbort(Intent intent) {
                                GT.logt("跳转被拦截");
                                //开始当前其他被拦截逻辑
                                return super.onAbort(intent);
                            }
                        });*/


                GT.ARouter.getInstance()
                        .build(Config.Model2Config.MAIN)
                        .putExtra("extra", "我的 extra")
                        .withObject("name", name)
                        .greenChannal()//绿色通过所有拦截器
                        .navigation();

                /*GT.ARouter.getInstance()
                        .build(Config.Model2Config.MAIN)
                        .putExtra("extra", "我的 extra")
                        .withObject("name", name)
                        .navigation(ModelActivity1.this,
                                new GT.ARouter.InterceptorCallback() {
                            @Override
                            public boolean onContinue(Intent intent) {
                                GT.logt("继续执行了");
                                return super.onContinue(intent);
                            }

                            @Override
                            public boolean onAbort(Intent intent) {
                                GT.logt("跳转被拦截");
                                return super.onAbort(intent);
                            }
                        });*/
            }
        });
    }

    @Override
    protected void onResultData(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onResultData(requestCode, resultCode, intent);
        String name = intent.getStringExtra("name");
        GT.logt("返回来的数据：" + name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GT.ARouter.getInstance().unregister(this);
    }
}