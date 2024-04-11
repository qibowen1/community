package com.qbwyyds.community.community.controller;

import com.qbwyyds.community.community.entity.Comment;
import com.qbwyyds.community.community.entity.DiscussPost;
import com.qbwyyds.community.community.entity.Page;
import com.qbwyyds.community.community.entity.User;
import com.qbwyyds.community.community.service.CommentService;
import com.qbwyyds.community.community.service.DiscussPostService;
import com.qbwyyds.community.community.service.LikeService;
import com.qbwyyds.community.community.service.UserService;
import com.qbwyyds.community.community.utils.CommunityConstant;
import com.qbwyyds.community.community.utils.CommunityUtil;
import com.qbwyyds.community.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping(path = "/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private HostHolder holder;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    //获取点赞
    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        System.out.println("111");
        User user = holder.getUser();
        if (user==null){
            return CommunityUtil.getJsonString(403,"你还未登录");
        }

        //创建disscucpost对象
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());

        //调用service
        discussPostService.addDiscussPost(discussPost);
        //错误后期统一处理
        return CommunityUtil.getJsonString(0,"发布成功");
    }

    //getDiscussPost 将帖子id通过路径传入
    //帖子详情
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(Model model, @PathVariable("discussPostId") int discussPostId, Page page){
        //根据帖子id获取帖子
        DiscussPost post = discussPostService.findDisscusPostByid(discussPostId);
        model.addAttribute("post",post);
        //获取发布帖子的user
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        //获取该帖子的点赞
        long entityLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount",entityLikeCount);
        //点赞状态 0表示未登录 也就是未点赞
        int entityLikeStatus = holder.getUser()==null? 0 : likeService.findEntityLikeStatus(holder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus",entityLikeStatus);
        //设置分页信息
        page.setPath("/discuss/detail/"+discussPostId);
        page.setLimit(5);
        page.setRows(post.getCommentCount());

        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 评论列表
        List<Comment> commentList= commentService.findCommentsByEntity(ENTITY_TYPE_POST,post.getId(),
                page.getOffset(),page.getLimit());
        //不能直接返回这个commentList， 需要添加额外信息，比如评论人 等等 // 评论VO列表
        List<Map<String, Object>> commentVoList=new ArrayList<>();
        if (commentList!=null){
            for (Comment comment:commentList){
                //评论Vo
                Map<String ,Object> commentVo=new HashMap<>();
                //评论
                commentVo.put("comment", comment);
                //坐着
                commentVo.put("user",userService.findUserById(comment.getUserId()));
                //点赞数量
                entityLikeCount=likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeCount",entityLikeCount);
                //点赞状态
                entityLikeStatus = holder.getUser()==null? 0 : likeService.findEntityLikeStatus(holder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus",entityLikeStatus);
                //回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(),
                        0, Integer.MAX_VALUE);
                //回复Vo列表
                List<Map<String, Object>> replyVolist =new ArrayList<>();

                //也需要对回复列表重新添加额外信息
                if (replyList!=null){
                    for (Comment reply :replyList){
                        Map<String, Object> replyVo=new HashMap<>();
                        //回复内容
                        replyVo.put("reply", reply);
                        //回复的作者
                        replyVo.put("user",userService.findUserById(reply.getUserId()));
                        //回复的目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target",target);
                        //点赞数量
                        entityLikeCount=likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeCount",entityLikeCount);
                        //点赞状态
                        entityLikeStatus = holder.getUser()==null? 0 : likeService.findEntityLikeStatus(holder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus",entityLikeStatus);
                        replyVolist.add(replyVo);
                    }
                }
                //将replyVoList放入comment中
                commentVo.put("replys",replyVolist);
                //获取评论评论数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_POST, comment.getEntityId());
                commentVo.put("replyCount",replyCount);
                //将vo加入到volist中
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVoList);

        return "/site/discuss-detail";
    }
}
