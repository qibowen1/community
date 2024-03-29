package com.qbwyyds.community.community;

import com.qbwyyds.community.community.dao.CommentMapper;
import com.qbwyyds.community.community.dao.DiscussPostMapper;
import com.qbwyyds.community.community.dao.LoginTicketMapper;
import com.qbwyyds.community.community.dao.UserMapper;
import com.qbwyyds.community.community.entity.Comment;
import com.qbwyyds.community.community.entity.DiscussPost;
import com.qbwyyds.community.community.entity.LoginTicket;
import com.qbwyyds.community.community.entity.User;
import com.qbwyyds.community.community.utils.CommunityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//加注解以原版配置运行
public class MapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);
    }
    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("戚搏文");
        user.setPassword("123456");
        user.setSalt("12345");
        user.setEmail("2564429454@qq.com");
        user.setHeaderUrl("http://images.nowcoder.com/head/100t.png");
        user.setCreateTime(new Date());
        System.out.println(user);
        int i = userMapper.insertUser(user);
        System.out.println(i);
    }
    @Test
    public void testUpdateHeaderUrl(){
        int i = userMapper.updateHeader(150, "http://images.nowcoder.com/head/101t.png");
        System.out.println(i);
    }
    @Test
    public void testUpdatePassword(){
        int i = userMapper.updatePassword(150, "qi201910ss");
        System.out.println(i);
    }

    @Test
    public void testDiscussPost(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(111, 0, 10);//offset=(页号-1)*页大小
        Iterator<DiscussPost> iterator = discussPosts.iterator();
        while (iterator.hasNext()){
            DiscussPost discussPost = iterator.next();
            System.out.println(discussPost);
        }

        int i = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(i);

        int i1 = discussPostMapper.selectDiscussPostRows(111);
        System.out.println(i1);
    }

    @Test
    public void testinsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(162);
        loginTicket.setTicket("abc1s23");
        loginTicket.setExpired(new Date());
        loginTicket.setStatus(0);
        int i = loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(loginTicket);
        LoginTicket loginTicket1 = loginTicketMapper.selectByTicket("abc1s23");
    }

    @Test
    public void test(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc1");
        System.out.println(loginTicket);
        loginTicketMapper.updateStatus("abc1",1);
        loginTicket = loginTicketMapper.selectByTicket("abc1");
        System.out.println(loginTicket);
    }

    @Test
    public void commenttest(){
        List<Comment> comments = commentMapper.selectCommentByEntity(1, 228, 0, 5);
        for (Comment comment:comments){
            System.out.println(comment);
        }
        int i = commentMapper.selectCountByEntity(1, 228);
        System.out.println(i);
    }
}
