package com.zephyr.mpbsblog.service;

import com.zephyr.mpbsblog.entity.Comment;
import com.zephyr.mpbsblog.vo.CommentVO;

import java.util.List;

/**
 * 评论业务接口
 * 提供评论的添加、查询和删除功能
 */
public interface CommentService {

    /**
     * 添加评论
     *
     * @param comment 评论实体
     */
    void addComment(Comment comment);

    /**
     * 根据文章ID获取评论树形结构
     *
     * @param postId 文章ID
     * @return 评论视图列表（树形结构）
     */
    List<CommentVO> getCommentTreeByPostId(Long postId);

    /**
     * 根据评论ID删除评论
     *
     * @param commentId 评论ID
     * @param userId    操作用户ID
     * @param isAdmin   是否为管理员（管理员可删除任何评论）
     * @return 删除是否成功
     */
    boolean deleteCommentById(Long commentId, Long userId, boolean isAdmin);
}
