package com.cy.dao;

import com.cy.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by cy
 * 2017/11/30 15:08
 */
public interface UserMapper {

    int deleteById(Long id);

    int insert(User user);

    List<User> findAll();

    User findById(Long id);

    int update(User user);
}
