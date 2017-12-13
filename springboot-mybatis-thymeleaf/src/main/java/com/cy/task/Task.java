package com.cy.task;

import com.mysql.jdbc.PerVmServerConfigCacheFactory;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Proxy;

/**
 * Created by cy
 * 2017/12/7 13:34
 */
@Component
@Log4j2
public class Task {

    @Autowired
    private AsyncTask asyncTask;

    private final static String URL = "http://top.baidu.com/buzz?b=341&c=513&fr=topbuzz_b1";
    private final static String URL_IP = "http://www.data5u.com/";
    private final static String URL_BLOG = "http://blog.csdn.net/yy1193889747/article/details/78785776";
    private final static String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Mobile Safari/537.36";


    /**
     * 测试
     */
    //@Scheduled(fixedRate = 60 * 60 * 1000)
    public void reportCurrentTime() throws Exception {
        long start = System.currentTimeMillis();
        asyncTask.taskOne();
        asyncTask.taskTwo();
        long end = System.currentTimeMillis();
        log.info("总耗时：{} 毫秒", end - start);
    }

    /**
     * 代理Ip爬取
     */
    // @Scheduled(fixedRate = 10 * 60 * 1000)
    public void ipProxy() throws IOException {
        Document doc = Jsoup.connect(URL_IP).userAgent(USER_AGENT).get();
        Elements ips = doc.select("body > div:nth-child(8) > ul > li:nth-child(2) > ul.l2").next();
        log.info("ip个数：{}", ips.size());
        for (int i = 0; i <= ips.size(); i++) {
            String ipaddr = ips.select("ul:nth-child(" + (i + 2) + ") > span:nth-child(1) > li").text();
            String proxy = ips.select("ul:nth-child(" + (i + 2) + ") > span:nth-child(2) > li").text();
            String speed = ips.select("ul:nth-child(" + (i + 2) + ") > span:nth-child(8) > li").text();
            log.info("ip: {}----端口: {} ----速度：{} ", ipaddr, proxy, speed);
            if (!"".equals(proxy)) {
                try {
                    Jsoup.connect(URL_BLOG).proxy(ipaddr, Integer.parseInt(proxy)).ignoreHttpErrors(false).timeout(3000).get();
                } catch (IOException e) {
                    log.info("不能用");
                }
            }
        }
    }

    /**
     * 百度今日热点爬取
     */
    //@Scheduled(fixedRate = 60 * 60 * 1000)
    public void baidu() throws Exception {
        Document doc = Jsoup.connect(URL).get();
        Elements news = doc.select("#main > div.mainBody > div > table > tbody > tr").next();
        for (Element newa : news) {
            String flag = newa.select("td.keyword > a.list-title").text();
            String url = newa.select("td.keyword > a.list-title").attr("href");
            String hot = newa.select("td.last > span").text();

            log.info("关键字: {}----地址: {} ----热度：{} ", flag, url, hot);

        }
    }

    public static void main(String[] args) throws Exception {
        Task task = new Task();
        task.ipProxy();

    }
    // @Scheduled(cron="0/5 * *  * * ? ") //每5秒执行一次
    // "0 0 12 * * ?" 每天中午十二点触发
    // "0 15 10 ? * *" 每天早上10：15触发
    // "0 15 10 * * ?" 每天早上10：15触发
    // "0 15 10 * * ? *" 每天早上10：15触发
    // "0 15 10 * * ? 2005" 2005年的每天早上10：15触发
    // "0 * 14 * * ?" 每天从下午2点开始到2点59分每分钟一次触发
    // "0 0/5 14 * * ?" 每天从下午2点开始到2：55分结束每5分钟一次触发
    // "0 0/5 14,18 * * ?" 每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
    // "0 0-5 14 * * ?" 每天14:00至14:05每分钟一次触发
    // "0 10,44 14 ? 3 WED" 三月的每周三的14：10和14：44触发
    // "0 15 10 ? * MON-FRI" 每个周一、周二、周三、周四、周五的10：15触发
}
