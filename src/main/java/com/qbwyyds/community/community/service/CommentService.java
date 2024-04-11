package com.qbwyyds.community.community.service;

import com.qbwyyds.community.community.dao.CommentMapper;
import com.qbwyyds.community.community.dao.DiscussPostMapper;
import com.qbwyyds.community.community.entity.Comment;
import com.qbwyyds.community.community.utils.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService implements CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit){
        return commentMapper.selectCommentByEntity(entityType,entityId,offset,limit);
    }

    public int findCommentCount(int entityType, int entityId){
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addCommentCount(Comment comment){
        if (comment==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //添加评论 去除html 和敏感词
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));

        int rows = commentMapper.insertComment(comment);

        //跟新之后 加上事务管理
        if (comment.getEntityType()==ENTITY_TYPE_POST){
            int count = commentMapper.selectCountByEntity(comment.getEntityType(),comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;

    }
}
