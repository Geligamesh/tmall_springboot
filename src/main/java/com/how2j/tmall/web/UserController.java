package com.how2j.tmall.web;

import com.how2j.tmall.pojo.User;
import com.how2j.tmall.service.UserService;
import com.how2j.tmall.util.Page4Navigator;
import com.how2j.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("users")
    public Page4Navigator<User> list(@RequestParam(value = "start",defaultValue = "0") int start,@RequestParam(value = "size",defaultValue = "5") int size){
        start = start < 0 ? 0:start;
        Page4Navigator<User> page = userService.list(start, size,5);
        return page;
    }


}
