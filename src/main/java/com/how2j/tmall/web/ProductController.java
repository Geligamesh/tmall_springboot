package com.how2j.tmall.web;

import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.service.ProductImageService;
import com.how2j.tmall.service.ProductService;
import com.how2j.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;

    @GetMapping("categories/{cid}/products")
    public Page4Navigator<Product> list(@PathVariable("cid") int cid, @RequestParam(value = "start",defaultValue = "0") int start,@RequestParam(value = "size",defaultValue = "5") int size){
        start = start < 0 ? 0 : start;
        Page4Navigator<Product> page = productService.list(cid,start,size,5);
        productImageService.setFirstProductImages(page.getContent());
        return page;
    }

    @GetMapping("products/{id}")
    public Product get(@PathVariable("id") int id){
        Product product = productService.get(id);
        return product;
    }

    @PostMapping("products")
    public Object add(@RequestBody Product bean){
        bean.setCreateDate(new Date());
        productService.add(bean);
        return bean;
    }

    @DeleteMapping("products/{id}")
    public Object delete(@PathVariable int id){
        productService.delete(id);
        return null;
    }

    @PutMapping("products")
    public Object update(@RequestBody Product bean){
        productService.update(bean);
        return bean;
    }
}
