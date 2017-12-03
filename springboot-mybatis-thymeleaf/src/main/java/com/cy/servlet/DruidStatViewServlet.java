package com.cy.servlet;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by cy
 * 2017/11/30 20:38
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*",
        initParams={
                @WebInitParam(name="allow",value="127.0.0.1"),// IP白名单 (没有配置或者为空，则允许所有访问)
                @WebInitParam(name="deny",value=""),// IP黑名单 (存在共同时，deny优先于allow)
                @WebInitParam(name="resetEnable",value="false"),// 禁用HTML页面上的“Reset All”功能
                @WebInitParam(name="login-username", value = "admin"),
                @WebInitParam(name="login-password", value = "admin")
        })
public class DruidStatViewServlet extends StatViewServlet {
}
