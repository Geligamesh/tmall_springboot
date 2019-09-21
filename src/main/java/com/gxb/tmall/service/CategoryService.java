package com.gxb.tmall.service;

import com.gxb.tmall.dao.CategoryDAO;
import com.gxb.tmall.pojo.Category;
import com.gxb.tmall.pojo.Product;
import com.gxb.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Cacheable(key="'categories-page-'+#p0+ '-' + #p1")
    public Page4Navigator<Category> list(int start,int size,int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Category> pageFromJPA = categoryDAO.findAll(pageable);
        return new Page4Navigator<>(navigatePages, pageFromJPA);
    }

    @Cacheable(key = "'categories-all'")
    public List<Category> list(){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return categoryDAO.findAll(sort);
    }

    /**
     * 保存一个分类
     * @param bean
     */
    @CacheEvict(allEntries = true)
    // @CachePut(key = "'category-one-'+#p0")
    public void add(Category bean){
        categoryDAO.save(bean);
    }

    /**
     * 删除一个分类
     * @param id
     */
    @CacheEvict(allEntries = true)
    // @CachePut(key = "'category-one-'+#p0")
    public void delete(int id){
        categoryDAO.delete(id);
    }

    /**
     * 根据id获取一个分类
     * @param id
     * @return
     */
    @Cacheable(key = "'categories-one-'+#p0")
    public Category get(int id){
        return categoryDAO.findOne(id);
    }

    /**
     * 根据id更新分类
     * @param bean
     */
    @CacheEvict(allEntries = true)
    // @CachePut(key = "'category-one-'+#p0")
    public void update(Category bean){
        categoryDAO.save(bean);
    }

    public void removeCategoryFromProduct(Category category) {
        List<Product> products =category.getProducts();
        if(null!=products) {
            for (Product product : products) {
                product.setCategory(null);
            }
        }

        List<List<Product>> productsByRow =category.getProductsByRow();
        if(null!=productsByRow) {
            for (List<Product> ps : productsByRow) {
                for (Product p: ps) {
                    p.setCategory(null);
                }
            }
        }
    }

    public void removeCategoryFromProduct(List<Category> categories){
        for (Category category : categories) {
            removeCategoryFromProduct(category);
        }
    }


}
