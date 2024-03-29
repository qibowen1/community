package com.qbwyyds.community.community.service;

import com.qbwyyds.community.community.dao.LoginTicketMapper;
import com.qbwyyds.community.community.dao.UserMapper;
import com.qbwyyds.community.community.entity.LoginTicket;
import com.qbwyyds.community.community.entity.User;
import com.qbwyyds.community.community.utils.CommunityConstant;
import com.qbwyyds.community.community.utils.CommunityUtil;
import com.qbwyyds.community.community.utils.MailClient;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int userId){
        return userMapper.selectById(userId);
    }

    public Map<String, Object> register(User user){
        Map<String,Object> map=new HashMap<>();
        if (user==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //用户名不能为空
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","用户名为空！");
            return map;
        }
        //用户名不能为空
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码为空！");
            return map;
        }
        //邮箱不能为空
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱为空！");
            return map;
        }
        //验证账号
        User user1 = userMapper.selectByUsername(user.getUsername());
        if (user1!=null){
            map.put("usernameMsg","该账号已经存在！");
            System.out.println(map.get("usernameMsg"));
            return map;
        }
        //验证账号
        User user2 = userMapper.selectByEmail(user.getEmail());
        if (user2!=null){
            map.put("emailMsg","邮箱已经存在！");
            return map;
        }

        //加密
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        int i = userMapper.insertUser(user);

        //激活邮件
        Context context=new Context(); //设置theyleaf的上下文
        context.setVariable("email",user.getEmail());
        //http://localhost:8080/community/activation/101/code
        String url=domain +contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        String content=templateEngine.process("/mail/activation",context);//生成模板
        mailClient.sendEmail(user.getEmail(),"激活账号",content);
        return map;
    }

    public int activation(int userid, String code){
        User user=userMapper.selectById(userid);
        if (user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userid,1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FALLURE;
        }
    }

    //登录
    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map=new HashMap<>();
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg", "账号不能为空");
            return map;
        } else if (StringUtils.isBlank(password)) {
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        //验证账号
        User user = userMapper.selectByUsername(username);
        if (user==null){
            map.put("usernameMsg", "该账号不存在");
            return map;
        }
        //验证状态
        if (user.getStatus()==0){
            map.put("usernameMsg", "该账号未激活");
            return map;
        }
        //验证密码
        String code = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(code)){
            map.put("passwordMsg","密码不正确");
            return map;
        }
        //生成登录凭证
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    //查询ticket
    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    //跟新头像
    public int updateHeader(int userId, String headerUrl){
        return userMapper.updateHeader(userId, headerUrl);
    }

    //修改密码
    public int updatePassword(int userId, String newPassword){
        return userMapper.updatePassword(userId,newPassword);
    }
}
