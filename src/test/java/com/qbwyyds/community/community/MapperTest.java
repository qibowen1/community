package com.qbwyyds.community.community;

import com.qbwyyds.community.community.dao.DiscussPostMapper;
import com.qbwyyds.community.community.dao.UserMapper;
import com.qbwyyds.community.community.entity.DiscussPost;
import com.qbwyyds.community.community.entity.User;
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
}
