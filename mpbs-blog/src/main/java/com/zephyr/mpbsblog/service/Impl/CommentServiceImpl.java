package com.zephyr.mpbsblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.entity.Comment;
import com.zephyr.mpbsblog.mapper.BlogPostMapper;
import com.zephyr.mpbsblog.mapper.CommentMapper;
import com.zephyr.mpbsblog.service.CommentService;
import com.zephyr.mpbsblog.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BlogPostMapper blogPostMapper; // 用于校验文章是否存在

    @Override
    public void addComment(Comment comment) {
        // 检查博客是否存在
        BlogPostEntity blogPost = blogPostMapper.selectById(comment.getPostId());
        if (blogPost == null) {
            throw new RuntimeException("博客不存在");
        }
        // 设置默认值
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentMapper.insert(comment);
    }

    @Override
    public List<CommentVO> getCommentTreeByPostId(Long postId) {
        List<CommentVO> flatList = commentMapper.getCommentVOsByPostId(postId);

        // 1. 构建一个 map<id, CommentVO>
        Map<Long, CommentVO> idMap = new HashMap<>();
        for (CommentVO comment : flatList) {
            comment.setChildren(new ArrayList<>());
            idMap.put(comment.getId(), comment);
        }

        // 2. 构建树状结构
        List<CommentVO> rootComments = new ArrayList<>();
        for (CommentVO comment : flatList) {
            if (comment.getParentId() == null) {
                rootComments.add(comment);
            } else {
                CommentVO parent = idMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getChildren().add(comment);
                }
            }
        }

        return rootComments;
    }

    @Override
    public boolean deleteCommentById(Long commentId, Long userId, boolean isAdmin) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            return false;
        }

        // 普通用户只能删除自己的评论，管理员可删除任何评论
        if (!comment.getUserId().equals(userId) && !isAdmin) {
            return false;
        }

        // 删除该评论及其所有子评论（递归）
        deleteWithChildren(commentId);
        return true;
    }

    private void deleteWithChildren(Long commentId) {
        // 先删子评论
        List<Comment> children = commentMapper.selectList(
                new QueryWrapper<Comment>().eq("parent_id", commentId)
        );
        for (Comment child : children) {
            deleteWithChildren(child.getId());
        }
        // 再删自己
        commentMapper.deleteById(commentId);
    }


}
