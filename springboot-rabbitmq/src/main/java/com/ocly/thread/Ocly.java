package com.ocly.thread;

/**
 * Created by cy
 * 2017/12/25 10:05
 */
public class Ocly extends Thread {

    // thread stop use flag
    volatile  boolean isok = true;
    @Override
    public void run() {
        System.out.println(getName() + " is actor");
        int count = 0;
        while (isok) {
            System.out.println(getName() + " appear " + (++count));
            if (count == 40) {
                isok = false;
            }
            if (count % 5 == 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(getName() + " play over");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Ocly();
        a.setName("Ocly");
        a.start();
        Wack wack = new Wack();
        Thread b = new Thread(wack,"Wack");
        b.start();
        Thread.sleep(3000);
        wack.isok=false;
    }
}

class Wack implements Runnable{
    volatile boolean isok = true;
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is actor");
        int count = 0;
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
