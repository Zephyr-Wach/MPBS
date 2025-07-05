package com.zephyr.mpbsgather.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("note")
@Data
public class NoteEntity {
    private String id;
    private String title;
    private String contentMd;
    private String isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
