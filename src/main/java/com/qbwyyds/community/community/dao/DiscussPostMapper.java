package com.qbwyyds.community.community.dao;

import com.qbwyyds.community.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //@Param("userId") 这是为参数取别名，另外 如果要配置动态sql，而且方法只有这一个参数，那么必须取别名！！！
    int selectDiscussPostRows(@Param("userId") int userId);
}
