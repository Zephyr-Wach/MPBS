package com.zephyr.mpbsblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.mpbsblog.entity.Comment;
import com.zephyr.mpbsblog.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    // 获取某篇博客的所有评论（按时间升序）
    @Select("SELECT c.*, u.user_name, u.avatar_url " +
            "FROM blog_comment c " +
            "JOIN user_info u ON c.user_id = u.user_id " +
            "WHERE c.post_id = #{postId} " +
            "ORDER BY c.created_at ASC")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "postId", column = "post_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "parentId", column = "parent_id"),
            @Result(property = "content", column = "content"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "avatarUrl", column = "avatar_url")
    })
    List<CommentVO> getCommentVOsByPostId(@Param("postId") Long postId);

}
