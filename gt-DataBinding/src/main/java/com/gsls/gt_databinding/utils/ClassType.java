package com.gsls.gt_databinding.utils;

public enum ClassType {

    ACTIVITY(0, "android.app.Activity"),
    FRAGMENT(1, "android.app.Fragment"),
    FRAGMENT_X(2, "androidx.fragment.app.Fragment"),
    SERVICE(3, "android.app.Service"),
    PROVIDER(4, "com.gsls.gt.GT.ARouter.IProvider"),
    INTERCEPTOR(5, "com.gsls.gt.GT.ARouter.IInterceptor"),
    CONTENT_PROVIDER(6, "android.app.ContentProvider"),
    METHOD(7, ""),
    UNKNOWN(-1, "Unknown route type");

    int id;
    String className;

    public int getId() {
        return id;
    }

    public ClassType setId(int id) {
        this.id = id;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public ClassType setClassName(String className) {
        this.className = className;
        return this;
    }

    ClassType(int id, String className) {
        this.id = id;
        this.className = className;
    }

    public static ClassType parse(String name) {
        for (ClassType routeType : ClassType.values()) {
            if (routeType.getClassName().equals(name)) {
                return routeType;
            }
        }

        return UNKNOWN;
    }
}
