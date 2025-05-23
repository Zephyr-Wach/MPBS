package com.zephyr.mpbsblog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BlogPostEntity {
    private String id;
    private String title;
    private String contentMd;
    private String contentHtml;
    private String coverUrl;
    private String authorId;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}
