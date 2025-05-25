package com.zephyr.mpbsblog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("blog_comment")
public class Comment {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId; // 若为 null 表示一级评论
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
