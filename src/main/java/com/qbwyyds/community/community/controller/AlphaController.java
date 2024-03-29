package com.qbwyyds.community.community.controller;

import com.qbwyyds.community.community.utils.CommunityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(path = "/alpha")
public class AlphaController {
    @RequestMapping(value = "/cookieTest",method = RequestMethod.GET)
    @ResponseBody
    public String test(HttpServletResponse response){
        //创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置cookie生效范围
        cookie.setPath("/community/alpha");
        //持续时间
        cookie.setMaxAge(60*10);
        //发送cookie
        response.addCookie(cookie);

        return "set cookie";
    }

    @RequestMapping(path = "/getCookie", method = RequestMethod.GET)
    @ResponseBody
    public String get(@CookieValue("code") String code){
        System.out.println(code);
        return "get cookie";
    }

    @RequestMapping(path = "/sessionTest",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession httpSession){//依赖自动注入 单例的 并且session是会话级别的，关闭浏览器session失效，第二次会重新创建！
        System.out.println(httpSession.getId());
        System.out.println(httpSession.getCreationTime());
        httpSession.setAttribute("id",3);
        httpSession.setAttribute("name","ass");
        return "set session";
    }

    @RequestMapping(path = "/getSession", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get Session";
    }

    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String ajaxTest(String name, int age){
        System.out.println(name+":"+age);
        return CommunityUtil.getJsonString(0,"操作成功！");
    }
}
