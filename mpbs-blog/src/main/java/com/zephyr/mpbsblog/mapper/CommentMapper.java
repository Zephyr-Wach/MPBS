package com.zephyr.mpbsblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.mpbsblog.entity.Comment;
import com.zephyr.mpbsblog.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 评论数据访问接口
 * 继承MyBatis-Plus的BaseMapper，提供基本CRUD功能。
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据文章ID获取评论视图列表
     *
     * @param postId 文章ID
     * @return 评论视图列表
     */
    List<CommentVO> getCommentVOsByPostId(@Param("postId") Long postId);
}

