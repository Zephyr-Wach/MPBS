package com.zephyr.mpbsapp.dto;

import com.zephyr.mpbsapp.entity.SidebarMenuEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SidebarMenuDto {
    private Long id;
    private String name;
    private String path;
    private String icon;
    private List<SidebarMenuDto> children = new ArrayList<>();
    private Long parentId;

    public SidebarMenuDto(SidebarMenuEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.path = entity.getPath();
        this.icon = entity.getIcon();
        this.parentId = entity.getParentId();
        this.children = new ArrayList<>();
    }
}