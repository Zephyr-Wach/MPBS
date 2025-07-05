package com.zephyr.mpbsgather.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("note_collection_relation")
@Data
public class RelationEntity {
    private String collectionId;
    private String noteId;
    private String orderIndex;
}
