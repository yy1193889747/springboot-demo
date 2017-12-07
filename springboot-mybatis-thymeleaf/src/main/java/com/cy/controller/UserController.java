package com.cy.controller;

import com.cy.entity.User;
import com.cy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cy
 * 2017/11/22 17:07
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String loginpage() {
        return "login";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/list")
    public String userList(Model model) {
        model.addAttribute("userList", userService.userList());
        return "data";
    }

    @GetMapping(value = "/toEdit")
    public String getUser(Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "user/userEdit";
    }

    @PostMapping(value = "/edit")
    public String editUser(User user) {
        userService.editUser(user);
        return "redirect:/list";
    }

    @GetMapping(value = "/delete")
    public String deleteUser(Long id) {
        userService.deleteUser(id);
        return "redirect:/list";
    }

    @PostMapping(value = "/add")
    public String addUser(User user) {
        userService.saveUser(user);
        return "redirect:/list";
    }

    @GetMapping(value = "/toAdd")
    public String addUser() {
        return "user/userAdd";
    }


}
