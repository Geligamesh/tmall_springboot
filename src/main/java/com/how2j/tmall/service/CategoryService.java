package com.how2j.tmall.service;

import com.how2j.tmall.dao.CategoryDAO;
import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    public Page4Navigator<Category> list(int start,int size,int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Category> pageFromJPA = categoryDAO.findAll(pageable);
        return new Page4Navigator<>(navigatePages, pageFromJPA);
    }

    public List<Category> list(){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return categoryDAO.findAll(sort);
    }

    /**
     * 保存一个分类
     * @param bean
     */
    public void add(Category bean){
        categoryDAO.save(bean);
    }

    /**
     * 删除一个分类
     * @param id
     */
    public void delete(int id){
        categoryDAO.delete(id);
    }

    /**
     * 根据id获取一个分类
     * @param id
     * @return
     */
    public Category get(int id){
        return categoryDAO.findOne(id);
    }

    /**
     * 根据id更新分类
     * @param bean
     */
    public void update(Category bean){
        categoryDAO.save(bean);
    }

    public void removeCategoryFromProduct(Category category){
        List<Product> products = category.getProducts();
        if(products!=null){
            for (Product product : products) {
                product.setCategory(null);
            }
        }

        List<List<Product>> productsByRow = category.getProductsByRow();
        for (List<Product> productList : productsByRow) {
            for (Product product : productList) {
                product.setCategory(null);
            }
        }
    }

    public void removeCategoryFromProduct(List<Category> categories){
        for (Category category : categories) {
            removeCategoryFromProduct(category);
        }
    }


}
