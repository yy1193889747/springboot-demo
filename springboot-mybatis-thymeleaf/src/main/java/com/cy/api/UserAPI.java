package com.cy.api;

import com.cy.entity.User;
import com.cy.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by cy
 * 2017/12/7 10:31
 */
@RestController
public class UserAPI {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户列表", notes = "sssswww")
    @GetMapping("/api/user")
    public List<User> getAll() {
        return userService.userList();
    }

    @ApiOperation(value = "通过Id获取用户信息", notes = "ssss")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/api/user/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
