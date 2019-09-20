package com.gxb.tmall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPageController {

    @GetMapping("admin")
    public String admin(){
        return "redirect:admin_category_list";
    }

    @GetMapping("admin_category_list")
    public String listCategory(){
        return "admin/listCategory";
    }

    @GetMapping("admin_category_edit")
    public String editCategory(){
        return "admin/editCategory";
    }

    @RequestMapping("/admin_order_list")
    public String listOrder(){
        return "admin/listOrder";
    }

    @RequestMapping("/admin_product_list")
    public String listProduct(){
        return "admin/listProduct";
    }

    @RequestMapping("/admin_product_edit")
    public String editProduct(){
        return "admin/editProduct";
    }

    @RequestMapping("/admin_productImage_list")
    public String listProductImage(){
        return "admin/listProductImage";
    }

    @RequestMapping("/admin_property_list")
    public String listProperty(){
        return "admin/listProperty";
    }

    @RequestMapping("/admin_property_edit")
    public String editProperty(){
        return "admin/editProperty";
    }

    @RequestMapping("/admin_propertyValue_edit")
    public String editPropertyValue(){
        return "admin/editPropertyValue";
    }

    @RequestMapping("/admin_user_list")
    public String listUser(){
        return "admin/listUser";
    }
}
