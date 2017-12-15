package com.cy.service;

import com.cy.entity.UserInfo;

import java.util.List;

/**
 * Created by cy
 * 2017/11/22 16:47
 */
public interface UserService {

    List<UserInfo> userList();

    UserInfo getUser(int id);

    int deleteUser(int id);

    UserInfo saveUser(UserInfo user);

    UserInfo editUser(UserInfo user);

    UserInfo findByUsername(String username);
}
