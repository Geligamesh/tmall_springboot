package com.how2j.tmall.service;

import com.how2j.tmall.dao.ReviewDAO;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewDAO reviewDAO;
    @Autowired
    private ProductService productService;

    public List<Review> list(Product product) {
        return reviewDAO.findByProductOrderByIdDesc(product);
    }

    public void add(Review review) {
        reviewDAO.save(review);
    }

    public int getCount(Product product) {
        return reviewDAO.countByProduct(product);
    }
}

