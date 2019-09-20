package com.gxb.tmall.dao;

import com.gxb.tmall.pojo.PropertyValue;
import com.gxb.tmall.pojo.Product;
import com.gxb.tmall.pojo.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyValueDAO extends JpaRepository<PropertyValue,Integer> {

    List<PropertyValue> findByProductOrderByIdDesc(Product product);
    PropertyValue findByProductAndProperty(Product product,Property property);
}
