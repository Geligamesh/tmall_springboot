package com.how2j.tmall.dao;

import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order,Integer> {

    public List<Order> findByUserAndStatusNotOrderByIdDesc(User user,String status);
}
