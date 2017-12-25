package com.ocly.modle;

import sun.security.jca.GetInstance;

/**
 * 饿汉模式，加载就创建 线程安全
 * Created by cy
 * 2017/12/25 9:11
 */
public class SingletonA {

    // 将构造方法私有化
    private SingletonA() {
    }
    // 创建唯一实例
    private static SingletonA instance = new SingletonA();

    //提供获取实例的方法
    public static SingletonA getInstance(){
        return instance;
    }

}
