package com.zephyr.mpbsblog.service;

import com.zephyr.mpbsblog.entity.Comment;
import com.zephyr.mpbsblog.vo.CommentVO;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);
    List<CommentVO> getCommentTreeByPostId(Long postId);
    boolean deleteCommentById(Long commentId, Long userId ,boolean isAdmin);
}
