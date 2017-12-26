package com.ocly.io;


import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.core.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by cy
 * 2017/12/26 9:40
 */
public class ThreadCopy extends Thread {
    int block;
    File fromfile;
    File tofile;
    int l = 100;

    public ThreadCopy(File tofile, int block) {
        this.tofile = tofile;
        this.block = block;

    }

    @Override
    public void run() {
        try {
            RandomAccessFile raf = new RandomAccessFile(tofile, "rw");
            raf.seek((block - 1) * 100);
            raf.writeBytes("this is block "+block+"\n");
            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
//        File file = new File("test.txt");
//        new ThreadCopy(file, 1).start();
//        new ThreadCopy(file, 3).start();
//        new ThreadCopy(file, 2).start();
//        new ThreadCopy(file, 4).start();

        String ss = "eeeeeeeedddd";
        int sd = StringUtils.indexOf(ss, "sd");
        System.out.println(sd);
    }
}
