package com.how2j.tmall.service;

import com.how2j.tmall.dao.OrderDAO;
import com.how2j.tmall.dao.OrderItemDAO;
import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.pojo.OrderItem;
import com.how2j.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete  = "delete";

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderItemDAO orderItemDAO;

    public Page4Navigator<Order> list(int start,int size,int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page page = orderDAO.findAll(pageable);
        return new Page4Navigator<>(navigatePages,page);
    }

    public void removeOrderFromOrderItem(Order order){
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(null);
        }
    }

    public void removeOrderFromOrderItem(List<Order> orders){
        for (Order order : orders) {
            removeOrderFromOrderItem(order);
        }
    }

    public Order get(int oid){
        return orderDAO.findOne(oid);
    }

    public void update(Order order){
        orderDAO.save(order);
    }
}
