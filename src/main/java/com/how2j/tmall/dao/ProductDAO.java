package com.how2j.tmall.dao;

import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDAO extends JpaRepository<Product,Integer> {
    Page<Product> findByCategory(Category category, Pageable pageable);
}