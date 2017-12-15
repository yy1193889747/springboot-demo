package com.cy.service.impl;

import com.cy.entity.UserInfo;
import com.cy.repository.UserRepository;
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

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public List<UserInfo> userList() {
        return userRepository.findAll();
    }

    @Override
    public UserInfo getUser(int uid) {
        String key = "UserInfo_" + uid;
        ValueOperations<String, UserInfo> operations = redisTemplate.opsForValue();
        // 缓存存在
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            UserInfo UserInfo = operations.get(key);
            LOGGER.info("从缓存中获取用户信息>> " + UserInfo.toString());
            return UserInfo;
        }
        // 插入缓存
        UserInfo UserInfo = userRepository.findByUid(uid);
        operations.set(key, UserInfo, 100, TimeUnit.SECONDS);
        LOGGER.info("用户信息插入缓存 >> " + UserInfo.toString());
        return UserInfo;
    }

    @Transactional
    @Override
    public int deleteUser(int id) {
        int ret = userRepository.deleteByUid(id);
        String key = "UserInfo_" + id;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            LOGGER.info("从缓存中删除用户 >> " + id);
        }
        return ret;
    }

    @Override
    public UserInfo saveUser(UserInfo UserInfo) {
        return userRepository.save(UserInfo);
    }

    @Override
    public UserInfo editUser(UserInfo UserInfo) {
        UserInfo UserInfo1 = userRepository.save(UserInfo);
        String key = "UserInfo_" + UserInfo.getUid();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            LOGGER.info("从缓存中删除用户 >> " + UserInfo1.toString());
        }
        return UserInfo1;
    }

    @Override
    public UserInfo findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
