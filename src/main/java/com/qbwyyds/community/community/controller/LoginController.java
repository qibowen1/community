package com.qbwyyds.community.community.controller;

import com.google.code.kaptcha.Producer;
import com.qbwyyds.community.community.config.KaptchaConfig;
import com.qbwyyds.community.community.entity.User;
import com.qbwyyds.community.community.service.UserService;
import com.qbwyyds.community.community.utils.CommunityConstant;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;


@Controller
public class LoginController implements CommunityConstant {
    public static final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @Autowired
    Producer kaptchaProducer;

    @RequestMapping(path = "/site/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(path = "/site/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    @Value("${server.servlet.context-path}")
    private String contextPath;

    //注册
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if (map==null || map.isEmpty()){
            model.addAttribute("msg","注册成功，已经向您的邮箱发送注册邮件，请尽快激活");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }


    //邮箱激活
    @RequestMapping(path = "/activation/{userid}/{code}",method = RequestMethod.GET)
    //PathVariable 从路径中取参数
    public String activation(Model model, @PathVariable("userid") int userid, @PathVariable("code") String code){
        int activation = userService.activation(userid, code);
        if (activation==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功成功，您的账号已经可以使用了");
            model.addAttribute("target","/site/login");

        }else if (activation==ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，该账号已经激活");
            model.addAttribute("target","/index");
        }else {
            model.addAttribute("msg","激活失败，激活码不正确");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    //生成验证码
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    @ResponseBody
    public void getKatcha(HttpServletResponse httpServletResponse, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session
        session.setAttribute("kaptcha",text);

        //将图片输出给浏览器
        httpServletResponse.setContentType("image/png");
        ServletOutputStream outputStream = null;
        try {
            outputStream = httpServletResponse.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            logger.error("验证码失败"+e.getMessage());
        }

    }

    //登录
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean remeberme,
                        Model model, HttpSession session, HttpServletResponse servletResponse){
        //判断验证码正确?
        String kaptch= (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(code)||StringUtils.isBlank(kaptch)||!code.equalsIgnoreCase(kaptch)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }
        //检查账号密码
        int expiredSceonds= remeberme ? REMEBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSceonds);
        if (map.containsKey("ticket")){
            Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSceonds);
            servletResponse.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }
    //退出
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/site/login";//默认get请求
    }
}
