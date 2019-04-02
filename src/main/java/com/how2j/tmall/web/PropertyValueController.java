package com.how2j.tmall.web;

import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.PropertyValue;
import com.how2j.tmall.service.ProductService;
import com.how2j.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertyValueController {

    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ProductService productService;

    @GetMapping("products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable("pid") int pid){
        Product product = productService.get(pid);
        propertyValueService.init(product);
        List<PropertyValue> bean = propertyValueService.findByProductOrderByIdDesc(product);
        return bean;
    }

    @PutMapping("propertyValues")
    public Object update(@RequestBody PropertyValue propertyValue){
        propertyValueService.update(propertyValue);
        return propertyValue;
    }
}
