package com.gxb.tmall.web;

import com.gxb.tmall.comparator.*;
import com.gxb.tmall.pojo.*;
import com.gxb.tmall.service.*;
import com.gxb.tmall.util.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    @GetMapping("forehome")
    public Object home() {
        List<Category> cs = categoryService.list();
        productService.fill(cs);
        productService.fillByRow(cs);
        categoryService.removeCategoryFromProduct(cs);
        return cs;
    }

    @PostMapping("foreregister")
    public Result register(@RequestBody User user) {
        String name = user.getName();
        String password = user.getPassword();
        HtmlUtils.htmlEscape(name);
        user.setName(name);

        if (userService.isExist(name)) {
            String message = "用户名已经存在，无法使用";
            return Result.fail(message);
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";
        String encodedPassword = new SimpleHash(algorithmName,password,salt,times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);
        return Result.success();
    }

    @PostMapping("forelogin")
    public Result login(@RequestBody User userPram, HttpSession session) {
        String name = userPram.getName();
        name = HtmlUtils.htmlEscape(name);
        userPram.setName(name);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name,userPram.getPassword());
        try {
            subject.login(token);
            User user = userService.findByName(name);
            session.setAttribute("user", user);
            return Result.success();
        }catch (AuthenticationException e) {
            String message = "账号密码有错误";
            return Result.fail(message);
        }
        // User user = userService.get(name, userPram.getPassword());
        //
        // if (user != null) {
        //     session.setAttribute("user", user);
        //     return Result.success();
        // } else {
        //     String message = "账号或者密码不正确";
        //     return Result.fail(message);
        // }
    }

    @GetMapping("foreproduct/{pid}")
    public Object product(@PathVariable("pid") int pid) {
        Product product = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> pvs = propertyValueService.findByProductOrderByIdDesc(product);
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProductImage(product);
        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("pvs", pvs);
        map.put("reviews", reviews);

        return Result.success(map);
    }

    @GetMapping("forecheckLogin")
    public Result checkLogin(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return Result.success();
        }
        return Result.fail("未登录");
        // User user = (User) session.getAttribute("user");
        // if (user != null) {
        //     return Result.success();
        // }
        // return Result.fail("请先登录");
    }

    @GetMapping("forecategory/{cid}")
    public Object category(@PathVariable int cid, String sort) {
        Category category = categoryService.get(cid);
        productService.fill(category);
        productService.setSaleAndReviewNumber(category.getProducts());
        categoryService.removeCategoryFromProduct(category);

        if (null != sort) {
            switch (sort) {
                case "review":
                    Collections.sort(category.getProducts(), new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(), new ProductDateComparator());
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(), new ProductSaleCountComparator());
                    break;
                case "price":
                    Collections.sort(category.getProducts(), new ProductPriceComparator());
                    break;
                case "all":
                    Collections.sort(category.getProducts(), new ProductAllComparator());
                    break;
            }
        }
        return category;
    }

    @GetMapping("foresearch")
    public Object search(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        List<Product> ps = productService.search(keyword, 0, 20);
        productImageService.setFirstProductImages(ps);
        return ps;
    }

    @GetMapping("forebuyone")
    public Object buyone(int pid, int num, HttpSession session) {
        return buyoneAndAddCart(pid, num, session);
    }

    @GetMapping("foreaddCart")
    public Object addCart(int pid, int num, HttpSession session) {
        buyoneAndAddCart(pid, num, session);
        return Result.success();
    }

    private Object buyoneAndAddCart(int pid, int num, HttpSession session) {

        Product product = productService.get(pid);
        int oiid = 0;

        User user = (User) session.getAttribute("user");
        boolean found = false;
        List<OrderItem> orderItems = orderItemService.listByUser(user);
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == product.getId()) {
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemService.update(orderItem);
                found = true;
                oiid = orderItem.getId();
                break;
            }
        }

        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setProduct(product);
            orderItem.setNumber(num);
            orderItemService.add(orderItem);
            oiid = orderItem.getId();
        }

        return oiid;
    }

    @GetMapping("forebuy")
    public Object buy(String[] oiid, HttpSession session) {
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;
        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem orderItem = orderItemService.get(id);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
            orderItems.add(orderItem);
        }
        productImageService.setFirstProductImagesOnOrderItems(orderItems);

        session.setAttribute("orderItems", orderItems);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("orderItems", orderItems);

        return Result.success(map);
    }

    @GetMapping("forecart")
    public Object cart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user);
        productImageService.setFirstProductImagesOnOrderItems(ois);
        return ois;
    }

    @GetMapping("forechangeOrderItem")
    public Object change(HttpSession session, int pid, int num) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.fail("未登录");
        }
        List<OrderItem> orderItems = orderItemService.listByUser(user);
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == pid) {
                orderItem.setNumber(num);
                orderItemService.update(orderItem);
                break;
            }
        }
        return Result.success();
    }

    @GetMapping("foredeleteOrderItem")
    public Object delete(HttpSession session, int oiid) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.fail("未登录");
        }
        orderItemService.delete(oiid);
        return Result.success();
    }

    @PostMapping("forecreateOrder")
    public Object createOrder(@RequestBody Order order, HttpSession session){
        System.out.println("开始创建一个订单");
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderService.waitPay);
        List<OrderItem> orderItems= (List<OrderItem>)  session.getAttribute("orderItems");
        System.out.println(orderItems);
        float total =orderService.add(order,orderItems);

        Map<String,Object> map = new HashMap<>();
        map.put("oid", order.getId());
        map.put("total", total);

        return Result.success(map);
    }

    @GetMapping("forepayed")
    public Object payed(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }

    @GetMapping("forebought")
    public Object bought(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.fail("未完成");
        }
        List<Order> orders = orderService.listByUserWithoutDelete(user);
        orderService.removeOrderFromOrderItem(orders);
        return orders;
    }

    @GetMapping("foreconfirmPay")
    public Object confirmPay(int oid) {
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        // orderService.cacl(order);
        orderService.removeOrderFromOrderItem(order);
        return order;
    }

    @GetMapping("foreorderConfirmed")
    public Object orderConfirmed(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitReview);
        order.setConfirmDate(new Date());
        orderService.update(order);
        return Result.success();
    }

    @PutMapping("foredeleteOrder")
    public Object deleteOrder(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.delete);
        orderService.update(order);
        return Result.success();
    }

    @GetMapping("forereview")
    public Object review(int oid) {
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        orderService.removeOrderFromOrderItem(order);
        Product product = order.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        Map<String,Object> map = new HashMap<>();
        map.put("o", order);
        map.put("p", product);
        map.put("reviews", reviews);
        return Result.success(map);
    }

    @PostMapping("foredoreview")
    public Object doreview(HttpSession session,int oid,int pid,String content) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.finish);
        orderService.update(order);

        Product product = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);

        User user = (User) session.getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setUser(user);
        review.setProduct(product);
        review.setCreateDate(new Date());
        reviewService.add(review);
        return Result.success();
    }

}
