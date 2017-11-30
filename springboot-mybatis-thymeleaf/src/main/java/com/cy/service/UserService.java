package com.cy.service;

import com.cy.entity.User;

import java.util.List;

/**
 * Created by cy
 * 2017/11/22 16:47
 */
public interface UserService {

    public List<User> userList();

    public User getUser(Long id);

    public int deleteUser(Long id);

    public int saveUser(User user);

    public int editUser(User user);
}
