package com.zephyr.mpbsblog.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SearchBlogVo {
    private String id;
    private String title;
    private String contentMd;
    private Date createdAt;
}
