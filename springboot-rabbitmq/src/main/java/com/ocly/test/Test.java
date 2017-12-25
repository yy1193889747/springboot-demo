package com.ocly.test;

import com.ocly.modle.SingletonA;
import com.ocly.modle.SingletonB;

/**
 * Created by cy
 * 2017/12/25 9:15
 */
public class Test {
    public static void main(String[] args) {
        SingletonA a = SingletonA.getInstance();
        SingletonA b = SingletonA.getInstance();
        if (a == b) {
            System.out.println("同一个");
        } else {
            System.out.println("不是同一个");
        }


    }
}
