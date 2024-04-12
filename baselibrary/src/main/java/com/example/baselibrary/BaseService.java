package com.example.baselibrary;


import com.gsls.gt.GT;

public interface BaseService extends GT.ARouter.IProvider {
    String sayHello(int value);

}
