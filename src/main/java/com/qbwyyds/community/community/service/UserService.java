package com.qbwyyds.community.community.service;

import com.qbwyyds.community.community.dao.UserMapper;
import com.qbwyyds.community.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int userId){
        return userMapper.selectById(userId);
    }

}
