package com.ocly.generic;

/**
 * 泛型
 *
 * Created by cy
 * 2017/12/26 14:49
 */

class Hello<T>{
    private T name;

    public T getName() {
        return name;
    }

    public void setName(T name) {
        this.name = name;
    }


}
public class GenericTest {
    public static void main(String[] args) {
        Hello name = new Hello();
        name.setName(new Hello());
        hello(name);

    }
    public static void hello(Hello h){
        System.out.println(h.getName());
    }
}
