package com.zephyr.mpbsgather.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NoteCollectionEntity {
    private String id;
    private String title;
    private String description;
    private String authorId;
    private String isPublic;
    private Date createdAt;
    private Date updatedAt;
}
