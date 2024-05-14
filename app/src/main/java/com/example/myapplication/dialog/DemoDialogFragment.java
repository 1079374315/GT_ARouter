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

    public static DemoDialogFragment newInstance(Bundle bundle) {
        DemoDialogFragment fragment = new DemoDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    //获取组件 与 findViewById 效果一样
    @GT.Annotations.GT_View(R.id.tv_data)
    private TextView tv_data;

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
            tv_data.setText(tv_data.getText() + "传递过来的名称：" + extra + " name:" + name);
        }

    }

    //这个 onClick 方法是GT库内置提供的一个专门负责为组件注册单击监听的方法，方法名是可以随意写的，只需要保证是 public void 即可
    @GT.Annotations.GT_Click({R.id.btn_ok, R.id.btn_cancel, R.id.tv_back})//对这些ID进行注册单击
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                Intent intent = new Intent();
                intent.putExtra("name", name);
                finish(intent);
                break;
            case R.id.btn_cancel:
            case R.id.tv_back:
                finish();//关闭对话框，并不返回任何数据
                break;
        }
    }

    @Override
    protected boolean onBackPressed() {
        GT.toast("监听到返回事件");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GT.ARouter.getInstance().unregister(this);

    }
}
