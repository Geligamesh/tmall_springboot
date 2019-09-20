package com.gxb.tmall.service;

import com.gxb.tmall.dao.PropertyDAO;
import com.gxb.tmall.dao.PropertyValueDAO;
import com.gxb.tmall.pojo.Product;
import com.gxb.tmall.pojo.PropertyValue;
import com.gxb.tmall.pojo.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueService {

    @Autowired
    private PropertyValueDAO propertyValueDAO;
    @Autowired
    private PropertyDAO propertyDAO;

    public void update(PropertyValue propertyValue){
        propertyValueDAO.save(propertyValue);
    }

    public PropertyValue findByProductAndProperty(Product product, Property property){
        return propertyValueDAO.findByProductAndProperty(product,property);
    }

    public List<PropertyValue> findByProductOrderByIdDesc(Product product){
        return propertyValueDAO.findByProductOrderByIdDesc(product);
    }

    public void init(Product product){
        List<Property> properties = propertyDAO.findByCategory(product.getCategory());
        for (Property property : properties) {
            PropertyValue propertyValue = propertyValueDAO.findByProductAndProperty(product, property);
            if(propertyValue==null){
                PropertyValue bean = new PropertyValue();
                bean.setProperty(property);
                bean.setProduct(product);
                propertyValueDAO.save(bean);
            }
        }
    }
}
