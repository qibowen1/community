package com.qbwyyds.community.community.dao;

import com.qbwyyds.community.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    //查询帖子评论

    /**
     * entity_type：评论目标的类别（如：1代表帖子，2代表评论等）
     * entity_id：具体目标（帖子id 228，或者帖子 id 229）
     * target_id：指向某个人的评论
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);

    //查询评论总数
    int selectCountByEntity(int entityType, int entityId);

}
