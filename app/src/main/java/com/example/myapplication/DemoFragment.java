package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import com.example.baselibrary.Config;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;

@GT_Route(value = Config.DemoFragment1.MAIN, extras = "Fragment 测试页")
@GT.Annotations.GT_AnnotationFragment(R.layout.fragment_demo)
public class DemoFragment extends GT.GT_Fragment.AnnotationFragment{

    @GT_Autowired
    private String name;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        GT.logt("初始化 DemoFragment");
        GT.ARouter.getInstance().inject(this); //与Autowired配合使用
        GT.logt("name:" + name);
    }
}
