package com.cy.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by cy
 * 2017/12/20 18:42
 */
public class HttpUtils {
    /**
     * java net 包 发送post请求
     *
     * @param path url
     * @param post xx=xx&yy=yy
     * @return 根据需要返回 页面 或者协议头
     */

    public static String netpost(String path, String post) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置禁止重定向
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(post);// post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            // 开始获取数据
            Map<String, List<String>> ss = httpURLConnection.getHeaderFields();
            List cookie = ss.get("Set-Cookie");
            for (int i = 0; i < cookie.size(); i++) {
                System.out.println(cookie.get(i).toString());
            }
            String cc = cookie.get(2).toString().substring(21, 53);
            System.out.println(cc);

            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                bos.write(arr, 0, len);
                bos.flush();
            }
            bos.close();
            System.out.println(bos.toString());
            // return bos.toString("utf-8");
            return cc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
