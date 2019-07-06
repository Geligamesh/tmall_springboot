package com.how2j.tmall.service;

import com.how2j.tmall.dao.ProductDAO;
import com.how2j.tmall.es.ProductESDAO;
import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.util.Page4Navigator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductESDAO productESDAO;


    public Page4Navigator<Product> list(int cid,int start,int size,int navigatorPages){
        Category category = categoryService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Product> page = productDAO.findByCategory(category,pageable);
        return new Page4Navigator<>(navigatorPages,page);
    }

    public void add(Product bean){
        productDAO.save(bean);
        productESDAO.save(bean);
    }

    public Product get(int id){
        return productDAO.findOne(id);
    }

    public void delete(int id) {
        productDAO.delete(id);
        productESDAO.delete(id);
    }

    public void update(Product bean){
        productDAO.save(bean);
        productESDAO.save(bean);
    }

    public List<Product> findByCategory(Category category){
        return productDAO.findByCategoryOrderById(category);
    }

    public void fill(Category category){
        List<Product> products = findByCategory(category);
        category.setProducts(products);
        productImageService.setFirstProductImages(products);
    }

    public void fill(List<Category> categories){
        for (Category category : categories) {
            fill(category);
        }
    }

    public void fillByRow(List<Category> categories){
        int productsNumberEachRow = 8;
        for (Category category : categories) {
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for(int i = 0; i < products.size(); i += productsNumberEachRow){
                int size = i + productsNumberEachRow;
                size = size > products.size() ? products.size() : size;
                List<Product> productsEachRow = products.subList(i, size);
                productsByRow.add(productsEachRow);
            }
            category.setProductsByRow(productsByRow);
        }

    }

    public void setSaleAndReviewNumber(Product product) {
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);

        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);
    }

    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products) {
            setSaleAndReviewNumber(product);
        }
    }

    public List<Product> search(String keyword,int start,int size) {
        // Sort sort = new Sort(Sort.Direction.DESC,"id");
        // Pageable pageable = new PageRequest(start,size,sort);
        // List<Product> products = productDAO.findByNameLike("%" + keyword + "%", pageable);
        // return products;
        initDatabase2ES();
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(QueryBuilders.matchPhraseQuery("name", keyword),ScoreFunctionBuilders.weightFactorFunction(100))
                .scoreMode("sum")
                .setMinScore(10);
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start, size,sort);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();

        Page<Product> page = productESDAO.search(searchQuery);
        return page.getContent();
    }

    private void initDatabase2ES(){
        Pageable pageable = new PageRequest(0, 5);
        Page<Product> page = productESDAO.findAll(pageable);
        //先从es中查找数据，如果es中没有数据再去数据库中查询数据
        if (page.getContent().isEmpty()) {
            List<Product> products = productDAO.findAll();
            for (Product product : products) {
                productESDAO.save(product);
            }
        }
    }

}
