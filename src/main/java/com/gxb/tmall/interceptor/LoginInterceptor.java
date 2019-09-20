package com.gxb.tmall.interceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        String contextPath = session.getServletContext().getContextPath();
        String[] requireAuthPages = new String[]{
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",

                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
        };
        String uri = httpServletRequest.getRequestURI();
        String page = StringUtils.remove(uri, contextPath + "/");
        if(beginWith(page, requireAuthPages)){
            Subject subject = SecurityUtils.getSubject();
            if(!subject.isAuthenticated()) {
                httpServletResponse.sendRedirect("login");
                return false;
            }
            // User user = (User) session.getAttribute("user");
            // if (user == null) {
            //     httpServletResponse.sendRedirect("login");
            //     return false;
            // }
        }
        return true;
    }

    private boolean beginWith(String page, String[] requireAuthPages) {
        boolean result = false;
        for (String requireAuthPage : requireAuthPages) {
            if(StringUtils.startsWith(page, requireAuthPage)){
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
