package com.how2j.tmall.dao;

import com.how2j.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User,Integer> {

    User findByName(String name);

    User findByNameAndPassword(String name,String password);
}
