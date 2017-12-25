package com.thread;

/**
 * Created by cy
 * 2017/12/25 10:05
 */
public class Ocly extends Thread {

    @Override
    public void run() {
        System.out.println(getName() + " is actor");
        int count = 0;
        Boolean isok = true;
        while (isok) {
            System.out.println(getName() + " appear " + (++count));
            if (count == 100) {
                isok = false;
            }
            if (count % 20 == 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(getName() + " play over");
    }

    public static void main(String[] args) {
        Thread a = new Ocly();
        a.setName("Ocly");
        a.start();
        Thread b = new Thread(new Wack(),"Wack");
        b.start();
    }
}

class Wack implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is actor");
        int count = 0;
        Boolean isok = true;
        while (isok) {
            System.out.println(Thread.currentThread().getName() + " appear " + (++count));
            if (count == 100) {
                isok = false;
            }
            if (count % 10 == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(Thread.currentThread().getName() + " play over");
    }
}
