package com.example.myapplication.bean;

import com.gsls.gt.GT;

@GT.Hibernate.GT_Bean//标识待Hibernate扫描的持久化类
public class Project {

    @GT.Hibernate.GT_Key(setAutoincrement = false)//主键标识,默认自增长，也可以使用 (setAutoincrement = false) 方法来设置是否自增长
    private String pId;

    //默认会自动映射到表中(博主方便演示，故使用 public 字段修饰，具体可按你自己习惯来写 get set)
    private String name;
    @GT.Hibernate.GT_Column(setNotInit = true)//标识不参与映射
    private String notValue;

    @Override
    public String toString() {
        return "Project{" +
                "pId=" + pId +
                ", name=" + name +
                '}';
    }
}
