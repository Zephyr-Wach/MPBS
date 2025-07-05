package com.zephyr.mpbsgather.dto;

import lombok.Data;

@Data
public class NoteDTO {
    private String title;
    private String contentMd;
    private String isPublic;
}
