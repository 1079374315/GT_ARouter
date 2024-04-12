package com.example.model1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.baselibrary.ARouter;
import com.example.baselibrary.Config;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.annotation.GT_R_Build;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;

import java.util.List;
import java.util.Map;

@GT_R_Build
@GT_Route(value = Config.Model1Config.ModelActivity1.MAIN, extras = "模块1主页")
@GT.Annotations.GT_AnnotationActivity(R2.layout.activity_model1)
public class ModelActivity1 extends GT.GT_Activity.AnnotationActivity {

    @GT_Autowired(value = Config.Model1Config.ModelActivity1.keyList, desc = "这是测试的集合参数")
    private List<Activity> listName;

    @GT_Autowired(Config.Model1Config.ModelActivity1.keyMap)
    private Map<String,Object> mapName;

    @GT_Autowired(Config.Model1Config.ModelActivity1.keyAAA)
    private String name;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        GT.logt("初始化 ModelActivity1");
        ARouter.getInstance().inject(this);//与Autowired配合使用
        GT.logt("nameA:" + name);//只有在 注入路由后才能使用该注解变量
        GT.logt("listName:" + listName);//只有在 注入路由后才能使用该注解变量
        GT.logt("mapName:" + mapName);//只有在 注入路由后才能使用该注解变量

        Intent intent = getIntent();
        if(intent != null){
            int keyBBB = intent.getIntExtra(Config.Model1Config.ModelActivity1.keyBBB, 0);
            boolean keyCCC = intent.getBooleanExtra(Config.Model1Config.ModelActivity1.keyCCC,false);
            GT.logt("keyBBB:" + keyBBB);
            GT.logt("keyCCC:" + keyCCC);
        }

        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(Config.Model2Config.MAIN)
                        .putExtra("extra","我的 extra")
                        .withObject("name",name)
                        .greenChannal()
//                        .greenChannal(Config.TestInterceptor.MAIN)
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
                        });
            }
        });
    }
}