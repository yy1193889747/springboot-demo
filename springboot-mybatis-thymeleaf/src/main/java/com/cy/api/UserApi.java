package com.cy.api;

import com.cy.entity.User;
import com.cy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by cy
 * 2017/12/7 10:31
 */
@RestController
public class UserApi {
    @Autowired
    private UserService userService;

    @GetMapping("/api/userlist")
    public List<User> getAll(){
        return userService.userList();
    }
}
