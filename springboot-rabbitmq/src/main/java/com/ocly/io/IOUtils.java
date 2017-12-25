package com.ocly.io;

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
        FileOutputStream out = new FileOutputStream(filename,true);
        out.write('A');
        //每次写一个字节
        out.close();
        IOUtils.printHex(filename);
    }

    /**
     * 拷贝文件
     * @param fromfile
     * @param tofile
     * @throws IOException
     */
    public static void copyFile(File fromfile, File tofile) throws IOException {
        if(!fromfile.exists()){
            throw new IllegalArgumentException("不存在");
        }
        if(!fromfile.isFile()){
            throw new IllegalArgumentException("不是文件");
        }
        FileInputStream in = new FileInputStream(fromfile);
        FileOutputStream out = new FileOutputStream(tofile);
        byte[] buf = new byte[8 * 1024];
        int b;
        while((b=in.read(buf,0,buf.length))!=-1){
            out.write(buf,0,b);
            out.flush();//字节流最好加上
        }
        in.close();
        out.close();
    }

    public static void main(String[] args) {
        try {
//            IOUtils.writeHex("H:\\mavenpro\\springboot-demo\\springboot-rabbitmq\\src\\main\\resources\\out.dat");
            IOUtils.copyFile(new File("H:\\mavenpro\\springboot-demo\\springboot-rabbitmq\\src\\main\\resources\\banner.txt"),new File
                    ("H:\\mavenpro\\springboot-demo\\springboot-rabbitmq\\src\\main\\resources\\out.txt"));
//            IOUtils.printHex("C:\\Users\\Administrator\\Desktop\\hhh.txt");
//            System.out.println("");
//            IOUtils.printHexArray("C:\\Users\\Administrator\\Desktop\\hhh.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
