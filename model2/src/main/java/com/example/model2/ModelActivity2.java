package com.example.model2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.baselibrary.ARouter;
import com.example.baselibrary.BaseService;
import com.example.baselibrary.Config;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.annotation.GT_R_Build;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;

@GT_R_Build
@GT_Route(value = Config.Model2Config.MAIN, interceptors = {Config.TestInterceptor.MAIN, Config.TestInterceptor2.MAIN}, extras = "模块2主页")
public class ModelActivity2 extends GT.GT_Activity.AnnotationActivity {

    //在初始化的时候直接默认启动 MainFragment
    @GT.GT_Fragment.Build(setLayoutMain = R2.id.fcv, setLayoutHome = R2.id.fcv)
    private GT.GT_Fragment gt_fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model2);
        GT.logt("初始化 ModelActivity2");

        Fragment demoFragment = ARouter.getInstance()
                .build(Config.DemoFragment1.MAIN)
                .withObject("name", "DemoFragment1 名字")
                .greenChannal()
                .navigation();

        GT.logt("demoFragment:" + demoFragment);

        Fragment modelFragment = ARouter.getInstance()
                .build(Config.Model1Config.ModelFragment1.MAIN)
                .withObject("name", "ModelFragment1 名字")
                .navigation();

        GT.logt("modelFragment:" + modelFragment);



        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GT.logt("单击了 modelFragment:" + modelFragment);
                gt_fragment.addCommit(R.id.fcv, modelFragment);
            }
        });

        findViewById(R.id.tv_getFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GT.logt("单击了 demoFragment:" + demoFragment);
                gt_fragment.addCommit(R.id.fcv, demoFragment);
            }
        });

    }

}