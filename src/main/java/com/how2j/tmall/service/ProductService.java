package com.how2j.tmall.service;

import com.how2j.tmall.dao.ProductDAO;
import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private CategoryService categoryService;

    public Page4Navigator<Product> list(int cid,int start,int size,int navigatorPages){
        Category category = categoryService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Product> page = productDAO.findByCategory(category,pageable);
        return new Page4Navigator<>(navigatorPages,page);
    }

    public void add(Product bean){
        productDAO.save(bean);
    }

    public Product get(int id){
        return productDAO.findOne(id);
    }

    public void delete(int id){
        productDAO.delete(id);
    }

    public void update(Product bean){
        productDAO.save(bean);
    }

}
