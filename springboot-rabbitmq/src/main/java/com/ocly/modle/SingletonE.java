package com.ocly.modle;

/**
 * 双重锁
 * <p>
 * Created by cy
 * 2017/12/25 9:47
 */
public class SingletonE {
    private SingletonE() {
    }

    private volatile static SingletonE singletonE;

    public static SingletonE getSingletonE() {
        if (singletonE == null) {
            synchronized (SingletonE.class) {
                if (singletonE == null) {
                    singletonE = new SingletonE();
                }
            }
        }
        return singletonE;
    }
}
