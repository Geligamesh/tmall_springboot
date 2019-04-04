package com.how2j.tmall.web;

import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.User;
import com.how2j.tmall.service.CategoryService;
import com.how2j.tmall.service.ProductService;
import com.how2j.tmall.service.UserService;
import com.how2j.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("forehome")
    public Object home(){
        List<Category> list = categoryService.list();
        productService.fill(list);
        productService.fillByRow(list);
        categoryService.removeCategoryFromProduct(list);
        return list;
    }

    @PostMapping("foreregister")
    public Object register(@RequestBody User user){
        String name = user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);
        if (exist){
            String message = "用户名已经存在，不能使用";
            return Result.fail(message);
        }
        user.setPassword(password);
        return Result.success();
    }
}
