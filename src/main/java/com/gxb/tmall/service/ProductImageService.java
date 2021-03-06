package com.gxb.tmall.service;

import com.gxb.tmall.dao.ProductImageDAO;
import com.gxb.tmall.pojo.OrderItem;
import com.gxb.tmall.pojo.Product;
import com.gxb.tmall.pojo.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageDAO productImageDAO;
    @Autowired
    private ProductService productService;

    public static final String type_single = "single";
    public static final String type_detail = "detail";

    public void add(ProductImage bean){
        productImageDAO.save(bean);
    }

    public void delete(int id){
        productImageDAO.delete(id);
    }

    public ProductImage get(int id){
        return productImageDAO.findOne(id);
    }

    public List<ProductImage> listSingleProductImages(Product product){
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product,type_single);
    }

    public List<ProductImage> listDetailProductImages(Product product){
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product,type_detail);
    }

    public void setFirstProductImage(Product product){
        List<ProductImage> singles = listSingleProductImages(product);
        if (!singles.isEmpty()){
            product.setFirstProductImage(singles.get(0));
        }else{
            product.setFirstProductImage(new ProductImage());
        }
    }

    public void setFirstProductImages(List<Product> products){
        for (Product product : products) {
            setFirstProductImage(product);
        }
    }

    public void setFirstProductImagesOnOrderItems(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            setFirstProductImage(orderItem.getProduct());
        }
    }
}
