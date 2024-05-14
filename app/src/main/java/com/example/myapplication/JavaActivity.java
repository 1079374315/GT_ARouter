package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baselibrary.Config;
import com.example.myapplication.notification.NotificationDemo;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.route.annotation.GT_Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@GT_Route(value = Config.AppConfig.MAIN ,extras = "app首页")
public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        findViewById(R.id.btn).setOnClickListener(v -> {
            ArrayList<Activity> list = new ArrayList<>();
            list.add(JavaActivity.this);
            Map<String,Object> map = new HashMap<>();
            map.put("1",111);
            map.put("2",222);
            map.put("3",333);


            GT.ARouter.getInstance()
                    .build(Config.Model1Config.ModelActivity1.MAIN)
                    .putExtra(Config.Model1Config.ModelActivity1.keyBBB, 1079)
                    .putExtra(Config.Model1Config.ModelActivity1.keyCCC, true)
                    .withObject(Config.Model1Config.ModelActivity1.keyAAA, "我是JavaActivity参数")
                    .withObject(Config.Model1Config.ModelActivity1.keyList, list)
                    .withObject(Config.Model1Config.ModelActivity1.keyMap, map)
                    .greenChannal()
                    .navigation();

        });

        findViewById(R.id.btn2).setOnClickListener(v -> {
            GT.logt("单击跳转2");
            //创建并发布 通知栏
            new NotificationDemo(this, null).commit();

//            startActivity(new Intent(JavaActivity.this, MainActivity.class));
        });

    }


}