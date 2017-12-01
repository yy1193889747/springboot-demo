package com.cy.service;

import com.cy.entity.User;

import java.util.List;

/**
 * Created by cy
 * 2017/11/22 16:47
 */
public interface UserService {

    List<User> userList();

    User getUser(Long id);

    int deleteUser(Long id);

    int saveUser(User user);

    int editUser(User user);
}
