package com.how2j.tmall.service;

import com.how2j.tmall.dao.CategoryDAO;
import com.how2j.tmall.dao.PropertyDAO;
import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo. Property;
import com.how2j.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyDAO propertyDAO;
    @Autowired
    private CategoryService categoryService;

    public Page4Navigator<Property> list(int cid,int start,int size,int navigatorPages){
        Category category = categoryService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Property> page = propertyDAO.getByCategory(category,pageable);
        return new Page4Navigator<>(navigatorPages,page);
    }

    public void add(Property bean){
        propertyDAO.save(bean);
    }

    public Property get(int id){
        Property bean = propertyDAO.findOne(id);
        return bean;
    }

    public void update(Property bean){
        propertyDAO.save(bean);
    }

    public void delete(int id){
        propertyDAO.delete(id);
    }

    public List<Property> listByCategory(Category category){
        return propertyDAO.findByCategory(category);
    }
}
