package com.qbwyyds.community.community.controller;

import com.qbwyyds.community.community.entity.DiscussPost;
import com.qbwyyds.community.community.entity.Page;
import com.qbwyyds.community.community.entity.User;
import com.qbwyyds.community.community.service.DiscussPostService;
import com.qbwyyds.community.community.service.LikeService;
import com.qbwyyds.community.community.service.UserService;
import com.qbwyyds.community.community.utils.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        //方法调用前 SpringMvc会自动实例化mode 和page, 传入的参数也会用于Page的实例化 并且将page注入model中
        //所以在thyleaf中可以直接访问page，不用model.addAttrubate；
        //int a=1/0;
        page.setPath("/index");
        page.setRows(discussPostService.findDiscussPostRows(0));

        List<DiscussPost> discussPostList=discussPostService.findDiscussPost(0,page.getOffset(),page.getLimit());
        List<Map<String, Object>> discussPosts=new ArrayList<>();
        if (discussPostList!=null){
            for(DiscussPost post:discussPostList){
                Map<String,Object> map=new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                long entityLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",entityLikeCount);//装入点赞数量
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "redirect:/error/500";
    }
}
