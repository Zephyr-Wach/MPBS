package com.zephyr.mpbsblog.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVO {
    private Long id;
    private Long postId;
    private Long userId;
    private String userName;      // 用户名
    private String avatarUrl;     // 头像
    private Long parentId;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentVO> children; // 子评论（递归结构）
}
