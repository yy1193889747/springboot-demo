package com.cy.util;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cy
 * 2017/12/20 18:51
 */
public class JsoupDemo {
    /**
     * Jsoup常用功能
     */
    public static void demo() throws IOException {
        /**
         * 关闭TSL SSL认证
         */
        Jsoup.connect("").validateTLSCertificates(false).execute();
        /**
         * 关闭重定向
         */
        Jsoup.connect("").followRedirects(false).execute();
        /**
         * get请求 execute默认就是get请求 返回Document
         */
        Document parse = Jsoup.connect("").execute().parse();
        Document parse1 = Jsoup.connect("").get();
        /**
         * get请求 返回json
         */
        String data1 = Jsoup.connect("").ignoreContentType(true).execute().body();
        /**
         * post请求 execute设置为post
         */
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", "");
        Document post = Jsoup.connect("").data(data).post();
        Document post1 = Jsoup.connect("").data("","").method(Connection.Method.POST).execute().parse();

        /**
         * post请求 json数据
         */
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("", "");
        Document post2 = Jsoup.connect("").requestBody(jsonObject.toString()).post();
        /**
         * 提交cookie 可以用map
         */
        Jsoup.connect("").cookie("","").execute();
        /**
         * 提交协议头 可以用map
         */
        Jsoup.connect("").header("","").execute();
        /**
         * 返回协议头 协议头居然没有cookie
         */
        Jsoup.connect("").execute().headers();
        /**
         * 返回cookie 因为map类型 同名的cookie只返回一个，这个比较坑
         */
        Jsoup.connect("").execute().cookies();


    }

}
