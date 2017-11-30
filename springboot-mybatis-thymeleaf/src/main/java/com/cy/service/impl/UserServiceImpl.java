package com.cy.service.impl;

import com.cy.dao.UserMapper;
import com.cy.entity.User;
import com.cy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by cy
 * 2017/11/22 16:50
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public List<User> userList() {
        return userMapper.findAll();
    }

    @Override
    public User getUser(Long id) {
        String key = "user_" + id;
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        // 缓存存在
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            User user = operations.get(key);
            LOGGER.info("从缓存中获取用户信息>> " + user.toString());
            return user;
        }
        // 插入缓存
        User user = userMapper.findById(id);
        operations.set(key, user, 100, TimeUnit.SECONDS);
        LOGGER.info("用户信息插入缓存 >> " + user.toString());
        return user;
    }

    @Override
    public int deleteUser(Long id) {
        int ret = userMapper.deleteById(id);
        String key = "user_" + id;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            LOGGER.info("从缓存中删除用户 >> " + id);
        }
        return ret;
    }

    @Override
    public int saveUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int editUser(User user) {
        int i = userMapper.update(user);
        String key = "user_" + user.getId();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            LOGGER.info("从缓存中删除用户 >> " + user.toString());
        }
        return i;
    }
}
