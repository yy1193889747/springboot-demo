package com.cy.repository;

import com.cy.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by cy
 * 2017/11/22 16:45
 */

public interface UserRepository extends JpaRepository<UserInfo,Integer>{

     UserInfo findByUid(int uid);

     UserInfo findByUsername(String username);

     int deleteByUid(int uid);

}
