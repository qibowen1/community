package com.qbwyyds.community.community.dao;

import com.qbwyyds.community.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper { //注解是mybatis 的@mapper 成为bean
    public User selectById(int id);

    public User selectByUsername(String username);

    public User selectByEmail(String email);

    public int insertUser(User user);

    public int updateStatus(int id, int status);

    public int updateHeader(int id, String headerUrl);

    public int updatePassword(int id, String password);
}
