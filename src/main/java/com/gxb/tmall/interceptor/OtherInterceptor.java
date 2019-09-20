package com.gxb.tmall.interceptor;

import com.gxb.tmall.pojo.Category;
import com.gxb.tmall.pojo.OrderItem;
import com.gxb.tmall.pojo.User;
import com.gxb.tmall.service.CategoryService;
import com.gxb.tmall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OtherInterceptor implements HandlerInterceptor {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderItemService orderItemService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        User user =(User) session.getAttribute("user");
        int  cartTotalItemNumber = 0;
        if(null!=user) {
            List<OrderItem> ois = orderItemService.listByUser(user);
            for (OrderItem oi : ois) {
                cartTotalItemNumber+=oi.getNumber();
            }

        }

        ServletContext context =httpServletRequest.getServletContext();
        List<Category> cs =categoryService.list();
        String contextPath = context.getContextPath();

        context.setAttribute("categories_below_search", cs);
        session.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
        context.setAttribute("contextPath", contextPath);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
