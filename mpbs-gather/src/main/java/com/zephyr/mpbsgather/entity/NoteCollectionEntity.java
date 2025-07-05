package com.zephyr.mpbsgather.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("note_collection")
@Data
public class NoteCollectionEntity {
    private String id;
    private String title;
    private String description;
    private String authorId;
    private String isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
