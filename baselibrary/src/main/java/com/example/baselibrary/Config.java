package com.example.baselibrary;

public interface Config {

    interface AppConfig{

        String MAIN_ACTIVITY = "/app/MainActivity";
        String MAIN = "/app/JavaActivity";

        String LIB1_SERVICE_IMPL_SAY_HELLO = "/app/Lib1ServiceImpl/sayHello";

        String BASE_VIEW = "/app/AgeView/";

        String DIALOG = "/app/DemoDialog/";
        String FLOATING = "/app/Floating/";

        String VIEW = "/app/CircleView/";

        String POPUP_WINDOW = "/app/PopupWindow/";

        String NOTIFICATION = "/app/Notification/";

    }

    interface DemoFragment1{
        String MAIN = "/app/DemoFragment/";
    }



    interface Model1Config{

        interface ModelActivity1{
            String MAIN = "/model1/ModelActivity1";
            String keyAAA = "aaa";
            String keyBBB = "bbb";
            String keyCCC = "ccc";
            String keyList = "list";
            String keyMap = "map";
        }

        interface ModelFragment1{
            String MAIN = "/model1/ModelFragment1/";
            String keyAAA = "aaa";
        }

    }

    interface Model2Config{
        String MAIN = "/model2/ModelActivity2";

    }

    interface TestInterceptor{
        String MAIN = "/model2/TestInterceptor/";
    }

    interface TestInterceptor2{
        String MAIN = "/app/TestInterceptor2";
    }

}
