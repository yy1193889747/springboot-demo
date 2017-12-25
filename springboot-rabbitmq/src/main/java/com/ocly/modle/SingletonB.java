package com.ocly.modle;

/**
 * 懒汉模式 线程不安全
 * Created by cy
 * 2017/12/25 9:19
 */
public class SingletonB {
    // 将构造方法私有化
    private SingletonB() {
    }

    // 申明唯一实例
    private static SingletonB instance;

    //提供获取实例的方法
    public static SingletonB getInstance() {
        if (instance == null) {
            instance = new SingletonB();
        }
        return instance;
    }
}
