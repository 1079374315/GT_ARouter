package com.example.myapplication;

import android.app.Application;

import com.gsls.gt.GT;
import com.gsls.gt_databinding.route.annotation.GT_RoutePath;

@GT_RoutePath  //标记路由总部所在 模块
public class MyApp extends Application {

    public void onCreate() {
        super.onCreate();
        GT.ARouter.init(this); /// 尽可能早，推荐在Application中初始化
    }

    public void onTerminate() {
        super.onTerminate();
        //释放资源
        GT.ARouter.getInstance().destroy();//释放资源路由全部资源
    }
}
