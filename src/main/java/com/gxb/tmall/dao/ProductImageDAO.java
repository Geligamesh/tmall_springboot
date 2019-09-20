package com.gxb.tmall.dao;

import com.gxb.tmall.pojo.ProductImage;
import com.gxb.tmall.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageDAO extends JpaRepository<ProductImage,Integer> {

    List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product,String type);
}
