package com.gxb.tmall.service;

import com.gxb.tmall.dao.UserDAO;
import com.gxb.tmall.pojo.User;
import com.gxb.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public Page4Navigator<User> list(int start, int size, int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<User> page = userDAO.findAll(pageable);
        return new Page4Navigator<>(navigatePages,page);
    }

    public void add(User user){
        userDAO.save(user);
    }

    public User findByName(String name) {
        return userDAO.findByName(name);
    }

    public boolean isExist(String name){
        User user = findByName(name);
        return user!=null;
    }

    public User get(String name,String password) {
        return userDAO.findByNameAndPassword(name, password);
    }
}
