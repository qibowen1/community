package com.qbwyyds.community.community.controller.interceptor;

import com.qbwyyds.community.community.entity.LoginTicket;
import com.qbwyyds.community.community.entity.User;
import com.qbwyyds.community.community.service.UserService;
import com.qbwyyds.community.community.utils.CookieUtil;
import com.qbwyyds.community.community.utils.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder holder;
    @Autowired
    private UserService userService;
    private static final Logger loger= LoggerFactory.getLogger(LoginInterceptor.class);
    //请求之前获取user， 并且暂存

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie获取
        String ticket= CookieUtil.getValue(request,"ticket");
        if (ticket!=null){
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //判断状态是否过期
            if (loginTicket!=null && loginTicket.getStatus()==0&&loginTicket.getExpired().after(new Date())){
                //查询用户
                int userId = loginTicket.getUserId();
                User user = userService.findUserById(userId);
                //在本次请求持有用户user 注意因为是并发的，所以要分隔开不同用户，用多线程 线程隔离 threadlocal
                holder.setUser(user);
            }
        }
        return true;
    }

    //controller之后 放入model

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = holder.getUser();
        if (user!=null && modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }
    }

    //请求结束清除user
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        holder.clear();
    }
}
