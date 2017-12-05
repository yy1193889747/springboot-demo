package com.cy.dao;

import com.cy.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by cy
 * 2017/11/30 15:08
 */
public interface UserMapper {
    @Delete("DELETE FROM user WHERE `id` = #{id}")
    int deleteById(@Param("id") Long id);

    @Insert("INSERT INTO user(`id`, `name`, `password`, `age`) VALUES (#{id}, #{name}, #{password}, #{age})")
    int insert(User user);

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("SELECT * FROM user WHERE `id` = #{id}")
    User findById(@Param("id") Long id);

    @Update("UPDATE user SET name = #{name}, password = #{password}, age = #{age}  WHERE id = #{id}")
    int update(User user);
}
