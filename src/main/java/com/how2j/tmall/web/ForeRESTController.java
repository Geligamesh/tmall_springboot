package com.how2j.tmall.web;

import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.service.CategoryService;
import com.how2j.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("forehome")
    public Object home(){
        List<Category> list = categoryService.list();
        productService.fill(list);
        productService.fillByRow(list);
        categoryService.removeCategoryFromProduct(list);
        return list;
    }
}
