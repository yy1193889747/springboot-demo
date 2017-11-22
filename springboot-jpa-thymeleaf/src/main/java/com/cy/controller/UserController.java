package com.cy.controller;

import com.cy.entity.User;
import com.cy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by cy
 * 2017/11/22 17:07
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

@GetMapping(value = "/list")
    public String userList(Model model){
    model.addAttribute("userList",userService.userList());
    return "user/userList";
    }
}
