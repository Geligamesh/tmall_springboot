package com.gxb.tmall.service;

import com.gxb.tmall.dao.OrderDAO;
import com.gxb.tmall.pojo.Order;
import com.gxb.tmall.pojo.OrderItem;
import com.gxb.tmall.pojo.User;
import com.gxb.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private OrderItemService orderItemService;

    public Page4Navigator<Order> list(int start, int size, int navigatePages){
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

    public void add(Order order) {
        orderDAO.save(order);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName = "Exception")
    public float add(Order order,List<OrderItem> orderItems) {
        float total = 0;
        add(order);

        if (false) {
            throw new RuntimeException();
        }

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
            orderItemService.update(orderItem);
            total += orderItem.getProduct().getPromotePrice();
        }
        return total;
    }

    public List<Order> listByUserAndNotDeleted(User user) {
        return orderDAO.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);
    }

    public List<Order> listByUserWithoutDelete(User user) {
        List<Order> orders = listByUserAndNotDeleted(user);
        orderItemService.fill(orders);
        return orders;
    }

    public void cacl(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        float total = 0l;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
        }
        order.setTotal(total);
    }

}
