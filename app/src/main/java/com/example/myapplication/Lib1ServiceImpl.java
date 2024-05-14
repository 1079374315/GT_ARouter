package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import com.example.baselibrary.BaseService;
import com.example.baselibrary.Config;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.route.annotation.GT_Route;


@GT_Route(value = Config.AppConfig.LIB1_SERVICE_IMPL_SAY_HELLO, extras = "接口测试页")
public class Lib1ServiceImpl implements BaseService {
    private Context mContext;

    @Override
    public String sayHello(int value) {
        GT.logt("mContext:" + mContext);
        if(mContext != null){
            Toast.makeText(mContext,"成功调用到 主模块 sayHello 方法", Toast.LENGTH_SHORT).show();
        }
        return "返回 sayHello:" + value;
    }

    @Override
    public void init(Context context) {
        GT.logt("init:" + context);
        mContext = context;
    }
}
