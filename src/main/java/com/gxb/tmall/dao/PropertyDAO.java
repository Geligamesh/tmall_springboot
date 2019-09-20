package com.gxb.tmall.dao;

import com.gxb.tmall.pojo.Category;
import com.gxb.tmall.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyDAO extends JpaRepository<Property,Integer> {

    Page<Property> getByCategory(Category category, Pageable pageable);
    List<Property> findByCategory(Category category);
}
