package com.how2j.tmall.dao;

import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewDAO extends JpaRepository<Review, Integer> {

    List<Review> findByProductOrderByIdDesc(Product product);
    int countByProduct(Product product);
}
