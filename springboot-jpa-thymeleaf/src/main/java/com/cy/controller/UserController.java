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
import org.springframework.web.bind.annotation.ResponseBody;

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


    @RequestMapping("/403")
    public @ResponseBody String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "403";
    }
    @RequestMapping({"/","/index"})
    public String index(){
        return"/index";
    }
}
