package com.cy.repository;

import com.cy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by cy
 * 2017/11/22 16:45
 */

public interface UserRepository extends JpaRepository<User,Integer>{

     User findByid(Long id);

     Long deleteByid(Long id);

}
