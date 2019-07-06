package com.how2j.tmall.dao;

import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageDAO extends JpaRepository<ProductImage,Integer> {

    List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product,String type);
}
