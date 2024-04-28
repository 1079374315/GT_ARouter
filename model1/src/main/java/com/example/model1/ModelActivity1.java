package com.example.model1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.example.baselibrary.ARouter;
import com.example.baselibrary.Config;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.annotation.GT_R_Build;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;
import com.gsls.toolkit.GT_Floating;

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

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
//        GT.logt("初始化 ModelActivity1");
        ARouter.getInstance().inject(this);//与Autowired配合使用
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
                //TODO 还有下面这些需要适配
                /**
                 String WebView = "WebView";
                 String Adapter = "Adapter";
                 ViewModel
                 */



                /*GT.GT_Notification.BaseNotification notification = ARouter.getInstance()
                        .build(Config.AppConfig.NOTIFICATION)
                        .putExtra("extra", "我的 extra 参数")//存储 Bundle 数据
                        .withObject("name", name)
                        .navigation(ModelActivity1.this);
                if (notification != null) {
                    notification.commit();
                }*/

                /*GT.GT_PopupWindow.BasePopupWindow popupWindow = ARouter.getInstance()
                        .build(Config.AppConfig.POPUP_WINDOW)
                        .putExtra("extra", "我的 extra 参数")// View 需要支持 存储 Bundle 数据
                        .withObject("name", name)
                        .navigation(ModelActivity1.this);
                if (popupWindow != null) {
                    popupWindow.startPopupWindowAbsolute(v, -100);
                }*/


                /*GT.GT_FloatingWindow.BaseFloatingWindow floatingWindow = ARouter.getInstance()
                        .build(Config.AppConfig.FLOATING)
                        .putExtra("extra", "我的 extra 参数")// View 需要支持 存储 Bundle 数据
                        .withObject("name", name)
                        .navigation();
                if (floatingWindow != null) {
                    GT_Floating.setTypeScreenType(GT.GT_FloatingWindow.BaseFloatingWindow.TYPE_DEFAULT);
                    startFloatingWindow(ModelActivity1.this, floatingWindow);
                }*/

                //验证参数传递
                /*View view = ARouter.getInstance()
                                .build(Config.AppConfig.VIEW)
                                .putExtra("extra", "我的 extra")// View 需要支持 存储 Bundle 数据
                                .withObject("name", name)
                                .navigation();
                GT.logt("view:" + view);
                if(view != null){
                    fl.addView(view);
                }*/


                /*DialogFragment dialogFragment =
                        ARouter.getInstance()
                                .build(Config.AppConfig.DIALOG)
                                .putExtra("extra", "我的 extra")
                                .withObject("name", name)
                                .navigation();
                GT.logt("dialogFragment:" + dialogFragment);
                if(dialogFragment != null){
                    startDialogFragment(dialogFragment);
                }*/


               /* GT.GT_View.BaseView gtView = ARouter.getInstance()
                                .build(Config.AppConfig.View)
                                .putExtra("extra","我的 extra")
                                .withObject("name", name)
                                .navigation();
                GT.logt("gtView:" + gtView);
                if(gtView != null){
                    gtView.init(ModelActivity1.this, fl);
                }*/

                //直接启动拦截器，可以直接用拦截器当过滤器
                /*ARouter.getInstance()
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


                /*ARouter.getInstance()
                        .build(Config.Model2Config.MAIN)
                        .putExtra("extra", "我的 extra")
                        .withObject("name", name)
                        .greenChannal()
                        .navigation(ModelActivity1.this, new GT.ARouter.InterceptorCallback() {
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
        ARouter.getInstance().unregister(this);
    }
}