package com.zephyr.mpbsblog.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private String id;
    private String postId;
    private String userId;
    private String parentId;
    private String content;
}
