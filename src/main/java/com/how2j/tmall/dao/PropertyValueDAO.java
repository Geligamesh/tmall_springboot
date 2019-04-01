package com.how2j.tmall.dao;

import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.Property;
import com.how2j.tmall.pojo.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyValueDAO extends JpaRepository<PropertyValue,Integer> {

    List<PropertyValue> findByProductOrderByIdDesc(Product product);
    PropertyValue findByProductAndProperty(Product product,Property property);
}
