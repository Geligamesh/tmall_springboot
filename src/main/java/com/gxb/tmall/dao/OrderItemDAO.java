package com.gxb.tmall.dao;

import com.gxb.tmall.pojo.Order;
import com.gxb.tmall.pojo.OrderItem;
import com.gxb.tmall.pojo.User;
import com.gxb.tmall.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemDAO extends JpaRepository<OrderItem,Integer> {

    List<OrderItem> findByOrderOrderByIdDesc(Order order);
    List<OrderItem> findByProduct(Product product);
    List<OrderItem> findByUserAndOrderIsNull(User user);
}
