package com.how2j.tmall.dao;

import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.pojo.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemDAO extends JpaRepository<OrderItem,Integer> {

    List<OrderItem> findByOrderOrderByIdDesc(Order order);
}
