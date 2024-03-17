package com.qbwyyds.community.community.service;

import com.qbwyyds.community.community.dao.DiscussPostMapper;
import com.qbwyyds.community.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    //This method returns a list of DiscussPost objects based on the given userId, offset, and limit parameters
    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit){
       //Invoke the selectDiscussPosts method from the DiscussPostMapper interface to retrieve the DiscussPost objects
       return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }



}
