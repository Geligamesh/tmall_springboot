package com.gxb.tmall.es;

import com.gxb.tmall.pojo.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductESDAO extends ElasticsearchRepository<Product,Integer> {
}
