package com.cy.test;

/**
 * 要优先使用基本类型而不是装箱基本类型，要担心无意识的自动装箱
 * <p>
 * Long 2305843005992468481--Use:8414
 * long 2305843005992468481--Use:744
 *
 * @author congyang.guo
 */
public class Test {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();

        long sum = 0L;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }
        System.out.println(sum + "--Use:" + (System.currentTimeMillis() - l));
    }
}
