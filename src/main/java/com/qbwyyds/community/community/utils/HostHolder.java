package com.qbwyyds.community.community.utils;

import com.qbwyyds.community.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 容器的作用 ，用于代替session
 */
@Component
public class HostHolder {
    //根据当前线程设置map
    private ThreadLocal<User> users=new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
