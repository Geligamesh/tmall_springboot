package com.how2j.tmall.web;

import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.service.OrderItemService;
import com.how2j.tmall.service.OrderService;
import com.how2j.tmall.util.Page4Navigator;
import com.how2j.tmall.util.Result;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("orders")
    public Page4Navigator<Order> list(@RequestParam(value = "start",defaultValue = "0") int start,@RequestParam(value = "size",defaultValue = "5") int size){
        start = start < 0 ? 0 : start;
        Page4Navigator<Order> page = orderService.list(start,size,5);
        orderItemService.fill(page.getContent());
        orderService.removeOrderFromOrderItem(page.getContent());
        return page;
    }

    @PutMapping("orders")
    public Object deliveryOrder(@RequestParam Order order){
        order.setStatus(orderService.waitDelivery);
        order.setDeliveryDate(new Date());
        orderService.update(order);
        return Result.success();
    }
}
