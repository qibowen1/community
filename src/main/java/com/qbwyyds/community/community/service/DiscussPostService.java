package com.qbwyyds.community.community.service;

import com.qbwyyds.community.community.dao.DiscussPostMapper;
import com.qbwyyds.community.community.entity.DiscussPost;
import com.qbwyyds.community.community.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    //This method returns a list of DiscussPost objects based on the given userId, offset, and limit parameters
    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit){
       //Invoke the selectDiscussPosts method from the DiscussPostMapper interface to retrieve the DiscussPost objects
       return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public int addDiscussPost(DiscussPost discussPost){
        //为空处理
        if (discussPost==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转义html标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        //敏感词过滤
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        return discussPostMapper.insertDisscussPost(discussPost);
    }

    //帖子详情
    public DiscussPost findDisscusPostByid(int id){
        return discussPostMapper.selectDisscusPostByid(id);
    }

    //跟新评论数
    public int updateCommentCount(int id, int commentCount){
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
}
