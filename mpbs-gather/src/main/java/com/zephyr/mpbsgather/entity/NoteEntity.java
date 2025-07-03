package com.zephyr.mpbsgather.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NoteEntity {
    private String id;
    private String title;
    private String contentMd;
    private String contentHtml;
    private String isPublic;
    private Date createdAt;
    private Date updatedAt;
}
