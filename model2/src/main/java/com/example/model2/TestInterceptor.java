package com.example.model2;

import android.content.Context;
import android.content.Intent;

import com.example.baselibrary.Config;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;

/**
 * 拦截要素：
 * 1.需要有  Context
 * 2.需要有上个页面跳转的 FragmentActivity 或 Fragment
 * 3.需要有上个页面传递通过 Intent 传递过来的数据进行判断逻辑
 * 4.可在拦截器上单独设置是否 异步 或 同步，不受排序影响
 * 5.拦截器的顺序按，被跳转页面上的 注解 拦截器排序顺序来执行
 * 6.能够让上个跳转页面进行监听 拦截器是否拦截 或 通过
 * 7.设计上要遵循 拦截器不受模块影响。
 */
@GT_Route(value = Config.TestInterceptor.MAIN, extras = "测试TestInterceptor拦截")
public class TestInterceptor implements GT.ARouter.Interceptor {

    @GT_Autowired
    private String name;

    @Override
    public void init(Context context, String injectObject) {
        GT.ARouter.getInstance().inject(this, injectObject);//与Autowired配合使用
    }

    @Override
    public boolean process(Intent intent, GT.ARouter.InterceptorCallback callback) {
        if(intent == null) return callback.onAbort(null);
        //需要的拦截器参数
        String extra = intent.getStringExtra("extra");
        GT.logt("extra:" + extra);
        GT.logt("name:" + name);
        //根据调用者提供的数据 判断登录状态

        //拦截器其他逻辑
        GT.logt("拦截通过");
        return callback.onContinue(intent);//已登录的 拦截通过
//        return callback.onAbort(intent);//未登录的 驳回不通过
    }
}
