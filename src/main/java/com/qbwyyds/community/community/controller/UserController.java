package com.qbwyyds.community.community.controller;

import com.qbwyyds.community.community.annotation.LoginRequire;
import com.qbwyyds.community.community.entity.User;
import com.qbwyyds.community.community.service.UserService;
import com.qbwyyds.community.community.utils.CommunityUtil;
import com.qbwyyds.community.community.utils.HostHolder;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.domain}")
    private String domain;
    @Value("${community.path.upload}")
    private String uplodaPath;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private HostHolder holder;
    @Autowired
    private UserService userService;
    //路由
    @LoginRequire
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getUsersetting(){
        return "/site/setting";
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    @LoginRequire
    public String uploadHeader(MultipartFile headerImage, Model model){
        System.out.println(headerImage);
        if (headerImage == null){
            model.addAttribute("error","还未选择图片!");
            return "/site/setting";
        }
        String originalFilename = headerImage.getOriginalFilename();
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }
        //随机生成文件名
        originalFilename= CommunityUtil.generateUUID() + suffix;
        //确定文件存放路径
        File dest=new File(uplodaPath+"/"+originalFilename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("文件上传失败",e);
            throw new RuntimeException("文件上传失败",e);
        }
        //跟新用户头像路径
        //http://localhost:8080/community/user/header/xxx.png格式
        User user = holder.getUser();
        String headerUrl= domain + contextPath + "/user/header/" + originalFilename;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }

    //获取头像
    @RequestMapping(path = "/header/{originalFilename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("originalFilename") String filename, HttpServletResponse response){
        //找到服务器上存放图片的路径 uploadPath
        filename = uplodaPath + "/" + filename;
        //输出图片
        String suffix = filename.substring(filename.lastIndexOf('.'));
        //相应图片
        response.setContentType("image/"+suffix);
        FileInputStream fis=null;
        //字节流
        try {
            ServletOutputStream os = response.getOutputStream();
            fis=new FileInputStream(filename);//获取文件输入流
            byte[] buffer =new byte[1024];//缓冲区
            int b=0;
            while ((b=fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败"+e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //修改密码
    @RequestMapping(path = "/uploadPassword", method = RequestMethod.POST)
    public String uploadPassword(Model model, String newPassword, String oldPassword){
        if (StringUtils.isBlank(newPassword)){
            model.addAttribute("error","新密码为空！");
            return "/site/setting";
        }
        User user = holder.getUser();
        if (!oldPassword.equals(user.getPassword())){
            model.addAttribute("error","原密码错误");
            return "/site/setting";
        }

        int i = userService.updatePassword(user.getId(), newPassword);
        if (i==0){
            model.addAttribute("error", "修改失败");
            return "/site/setting";
        }else {
            return "redirect:/login";//修改成功
        }
    }
}
