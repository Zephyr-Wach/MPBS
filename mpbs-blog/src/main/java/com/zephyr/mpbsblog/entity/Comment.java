package com.zephyr.mpbsblog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("blog_comment")
public class Comment {
    private String id;
    private String postId;
    private String userId;
    private String parentId;
    private String content;
    private LocalDateTime createdAt;
}
