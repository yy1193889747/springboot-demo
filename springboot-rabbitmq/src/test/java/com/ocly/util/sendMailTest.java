package com.ocly.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by cy
 * 2017/12/13 20:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class sendMailTest {


    @Autowired
    private SendMail sendMail;

    @Test
    public void hello() throws Exception {
        sendMail.sendSimpleMail("aaaa","1193889747@qq.com","sdas");
    }

    @Test
    public void hellomaiTemplate() throws Exception {
        sendMail.sendtemplateHtmlMail("你好，模板邮件加附件加图片","1193889747@qq.com","Bocly");
    }
}