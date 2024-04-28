package com.example.myapplication.notification;


import android.content.Context;
import android.os.Bundle;

import com.example.baselibrary.ARouter;
import com.example.baselibrary.Config;
import com.example.myapplication.R;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;


@GT_Route(value = Config.AppConfig.NOTIFICATION, extras = "通知栏")
//加载 折叠通知栏 与 展开通知栏
@GT.Annotations.GT_Notification(value = R.layout.item_notification, value2 = R.layout.item_notification2)
public class NotificationDemo extends GT.GT_Notification.AnnotationNotification {

    @GT_Autowired
    private String name;

    public NotificationDemo(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);

        ARouter.getInstance().inject(this); //与Autowired配合使用
        GT.logt("name:" + name);

        Bundle arguments = getArguments();
        GT.logt("arguments:" + arguments);
        if(arguments != null){
            String extra = arguments.getString("extra");
            GT.logt("extra:" + extra);

        }

        //初始化通知栏 必要属性
        setInitData(
                R.mipmap.ic_launcher, //设置通知图标
                true,//单击是否取消通知
                true,//锁屏是否显示
                false,//是否用户不可取消通知
                null,//单击意图
                -1,//发送通知时间
                false//是否悬浮式弹出
                ,222);//通知栏编号Id



    }

}
