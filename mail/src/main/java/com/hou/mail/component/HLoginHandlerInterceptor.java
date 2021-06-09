package com.hou.mail.component;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * get a login status
 */
public class HLoginHandlerInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //before method execute
//        System.out.println("loading basic user info.");
        Object user = request.getSession().getAttribute("userMes");
        if(user == null){
            request.setAttribute("errorMes","无权限，请登录");
            request.getRequestDispatcher("/index").forward(request, response);
            return false;
            //return
        }else return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
