package com.example.myapplication.dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.baselibrary.Config;
import com.example.myapplication.R;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;

@GT_Route(value = Config.AppConfig.DIALOG, extras = "对话框测试")
@GT.Annotations.GT_AnnotationDialogFragment(R.layout.demo_dialog_fragment)
public class DemoDialogFragment extends GT.GT_Dialog.AnnotationDialogFragment {

    @GT_Autowired
    private String name;

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        GT.ARouter.getInstance().inject(this); //与Autowired配合使用
        setHideBackground();//隐藏背景(GT内置提供的功能方法)
        setClickExternalNoHideDialog();//设置点击外部无效(GT内置提供的功能方法)
        Bundle bundle = getArguments();
        if (bundle != null) {
            String extra = bundle.getString("extra");
        }

    }

}
