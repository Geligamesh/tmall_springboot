package com.gxb.tmall.dao;

import com.gxb.tmall.pojo.Order;
import com.gxb.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order,Integer> {

    public List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}
