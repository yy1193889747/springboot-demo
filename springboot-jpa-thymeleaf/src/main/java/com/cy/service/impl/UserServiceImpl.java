package com.cy.service.impl;

import com.cy.entity.User;
import com.cy.repository.UserRepository;
import com.cy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cy
 * 2017/11/22 16:50
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> userList() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findByid(id);
    }
    @Transactional
    @Override
    public Long deleteUser(Long id) {
        return userRepository.deleteByid(id);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User editUser(User user) {
        return userRepository.save(user);
    }
}
