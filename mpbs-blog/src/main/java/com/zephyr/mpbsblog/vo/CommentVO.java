package com.zephyr.mpbsblog.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVO {
    private String id;
    private String postId;
    private String userId;
    private String userName;      // 用户名
    private String avatarUrl;     // 头像
    private String parentId;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentVO> children; // 子评论（递归结构）
}
