package com.cy.controller;

import com.cy.entity.UserInfo;
import com.cy.service.UserService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by cy
 * 2017/11/22 17:07
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/userInfo/userList")
    @RequiresPermissions("userInfo:view")
    public String userList(Model model) {
        model.addAttribute("userList", userService.userList());
        return "user/userList";
    }

    @GetMapping(value = "/toEdit")
    public String getUser(int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "user/userEdit";
    }

    @PostMapping(value = "/edit")
    public String editUser(UserInfo user) {
        System.out.println(user.getName());
        userService.editUser(user);
        return "redirect:/list";
    }

    @GetMapping(value = "/delete")
    public String deleteUser(int id) {
        userService.deleteUser(id);
        return "redirect:/list";
    }

    @PostMapping(value = "/add")
    public String addUser(UserInfo user) {
        userService.saveUser(user);
        return "redirect:/list";
    }

    @GetMapping(value = "/toAdd")
    public String addUser() {
        return "user/userAdd";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Map<String, Object> map) throws Exception {
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        System.out.println("exception=" + exception);
        String msg = "没处理";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                System.out.println("UnknownAccountException -- > 账号不存在：");
                msg = "UnknownAccountException -- > 账号不存在：";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
                msg = "IncorrectCredentialsException -- > 密码不正确：";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                System.out.println("kaptchaValidateFailed -- > 验证码错误");
                msg = "kaptchaValidateFailed -- > 验证码错误";
            } else {
                msg = "else >> " + exception;
                System.out.println("else -- >" + exception);
            }
        }
        map.put("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理
        return "/login";
    }

    @RequestMapping("/403")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "403";
    }
    @RequestMapping({"/","/index"})
    public String index(){
        return"/index";
    }
}
