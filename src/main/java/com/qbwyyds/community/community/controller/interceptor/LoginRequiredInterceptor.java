package com.qbwyyds.community.community.controller.interceptor;

import com.qbwyyds.community.community.annotation.LoginRequire;
import com.qbwyyds.community.community.utils.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    HostHolder holder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequire annotation = method.getAnnotation(LoginRequire.class);
            if (annotation != null &&holder.getUser()==null){
                response.sendRedirect(request.getContextPath()+"/site/login");
                return false;
            }
        }
        return true;
    }
}
