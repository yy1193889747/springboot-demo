package com.ocly.io;

import java.awt.*;
import java.io.*;

/**
 * Created by cy
 * 2017/12/25 14:00
 */
public class IOUtils {

    /**
     * 读取文件内容，以16进制显示
     *
     * @param filename
     */
    public static void printHex(String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        int b;
        int i = 1;
        while ((b = in.read()) != -1) {
            System.out.print(String.format("%02x ", b));
            if (i++ % 10 == 0) {
                System.out.println();
            }
        }
        in.close();
    }

    /**
     * 读取文件内容到数组，以16进制显示
     *
     * @param filename
     */
    public static void printHexArray(String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        byte[] buf = new byte[20 * 1024];
    /*  从in中批量读取字节，放到buf数组中，
        从0开始 最多 buf.length个
        返回读到的字节个数*/
        int b = in.read(buf, 0, buf.length);
        int i = 1;
        for (int j = 0; j < b; j++) {

            System.out.print(String.format("%02x ", buf[j]));
            if (i++ % 10 == 0) {
                System.out.println();
            }
        }
        in.close();
    }

    /**
     * 写到文件
     *
     * @param filename
     */
    public static void writeHex(String filename) throws IOException {
        // 没有就创建，有的话 删了再创建，true则是追加
        FileOutputStream out = new FileOutputStream(filename, true);
        out.write('A');
        out.write(10);
        //每次写一个字节
        out.close();
        IOUtils.printHex(filename);
    }

    /**
     * 拷贝文件 批量+缓存
     *
     * @param fromfile
     * @param tofile
     * @throws IOException
     */
    public static void copyFile(File fromfile, File tofile) throws IOException {
        if (!fromfile.exists()) {
            throw new IllegalArgumentException("不存在");
        }
        if (!fromfile.isFile()) {
            throw new IllegalArgumentException("不是文件");
        }
        FileInputStream in = new FileInputStream(fromfile);
        FileOutputStream out = new FileOutputStream(tofile);
        byte[] buf = new byte[8 * 1024];
        int b;
     /*   public int read(byte b[]) throws IOException {
            return readBytes(b, 0, b.length);
        }*/
        while ((b = in.read(buf)) != -1) {
            out.write(buf, 0, b);
        }
        out.flush();//字节流最好加上
        in.close();
        out.close();
    }

    /**
     * 拷贝文件 buff缓存
     *
     * @param fromfile
     * @param tofile
     * @throws IOException
     */
    public static void copyFileByBuff(File fromfile, File tofile) throws IOException {
        if (!fromfile.exists()) {
            throw new IllegalArgumentException("不存在");
        }
        if (!fromfile.isFile()) {
            throw new IllegalArgumentException("不是文件");
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fromfile));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tofile));
        int b;
        while ((b = bis.read()) != -1) {
            bos.write(b);
        }
        //bos.flush();//字节流最好加上
        bis.close();
        bos.close();
    }

    /**
     * 拷贝文本文件 buff缓存
     *
     * @param fromfile
     * @param tofile
     * @throws IOException
     */
    public static void copytxtFileByBuff(File fromfile, File tofile) {
        if (!fromfile.exists()) {
            throw new IllegalArgumentException("不存在");
        }
        if (!fromfile.isFile()) {
            throw new IllegalArgumentException("不是文件");
        }
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(fromfile));
            OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(tofile));

            BufferedReader bs = new BufferedReader(is);
//            BufferedWriter bw = new BufferedWriter(os);
            PrintWriter pw = new PrintWriter(os,true);
            String input;
            while ((input = bs.readLine()) != null) {
                pw.println(input);
            }
//            bw.flush();
//            bw.close();
            pw.close();
            bs.close();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 写到文件,dataoutputstream
     *
     * @param filename
     */
    public static void writeInfile(String filename) throws IOException {
        // 没有就创建，有的话 删了再创建，true则是追加
        DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));
        out.writeInt(10);
        out.writeUTF("你好"); // utf-8
        out.writeChars("你好"); // utf-16
        out.close();
        IOUtils.printHex("C:\\Users\\Administrator\\Desktop\\hhh.txt");
    }

    public static void readfile(String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        InputStreamReader is = new InputStreamReader(in, "utf-8");
        int b;
        while ((b = is.read()) != -1) {
            System.out.print((char) b);
        }
        in.close();
        is.close();
    }

    public static void main(String[] args) {
        try {
//            IOUtils.writeHex("H:\\mavenpro\\springboot-demo\\springboot-rabbitmq\\src\\main\\resources\\out.dat");
//            IOUtils.copyFile(new File("H:\\mavenpro\\springboot-demo\\springboot-rabbitmq\\src\\main\\resources\\banner.txt"),new File
//                    ("H:\\mavenpro\\springboot-demo\\springboot-rabbitmq\\src\\main\\resources\\out.txt"));
//            IOUtils.printHex("C:\\Users\\Administrator\\Desktop\\hhh.txt");
//            System.out.println("");
//            IOUtils.printHexArray("C:\\Users\\Administrator\\Desktop\\hhh.txt");
//            IOUtils.writeInfile("C:\\\\Users\\\\Administrator\\\\Desktop\\\\hhh.txt");

//            Long start = System.currentTimeMillis();
//            IOUtils.copyFile(new File("C:\\Users\\Administrator\\Desktop\\1.mp3"), new File("C:\\Users\\Administrator\\Desktop\\4.mp3"));
//            Long end = System.currentTimeMillis();
//            System.out.println(end - start);
            IOUtils.copytxtFileByBuff(new File("C:\\Users\\Administrator\\Desktop\\hhh.txt"), new File("C:\\Users\\Administrator\\Desktop\\hhhs.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
