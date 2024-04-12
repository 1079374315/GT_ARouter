package com.example.myapplication;

import android.app.Application;

import com.example.baselibrary.ARouter;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.annotation.GT_R_Build;

//@GT_R_Build
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        //释放资源
        ARouter.getInstance().destroy();
    }
}
