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

    public Long deleteUser(Long id);

    public User saveUser(User user);

    public User editUser(User user);
}
