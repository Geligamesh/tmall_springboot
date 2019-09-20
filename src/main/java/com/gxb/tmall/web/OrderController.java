package com.gxb.tmall.web;

import com.gxb.tmall.pojo.Order;
import com.gxb.tmall.service.OrderItemService;
import com.gxb.tmall.service.OrderService;
import com.gxb.tmall.util.Page4Navigator;
import com.gxb.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("orders")
    public Page4Navigator<Order> list(@RequestParam(value = "start",defaultValue = "0") int start, @RequestParam(value = "size",defaultValue = "5") int size){
        start = start < 0 ? 0 : start;
        Page4Navigator<Order> page = orderService.list(start,size,5);
        orderItemService.fill(page.getContent());
        orderService.removeOrderFromOrderItem(page.getContent());
        return page;
    }

    // @PutMapping("deliveryOrder/{oid}")
    // public Object deliveryOrder(@PathVariable int oid){
    //     Order order = orderService.get(oid);
    //     order.setStatus(orderService.waitDelivery);
    //     order.setDeliveryDate(new Date());
    //     orderService.update(order);
    //     return Result.success();
    // }

    @PutMapping("orders")
    public Object deliveryOrder(@RequestBody Order order){
        order.setStatus(orderService.waitDelivery);
        order.setDeliveryDate(new Date());
        orderService.update(order);
        return Result.success();
    }

}
