package com.how2j.tmall.dao;

import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyDAO extends JpaRepository<Property,Integer> {

    Page<Property> getByCategory(Category category,Pageable pageable);
}
