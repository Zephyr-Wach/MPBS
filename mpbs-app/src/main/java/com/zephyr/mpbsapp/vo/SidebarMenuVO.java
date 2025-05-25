package com.zephyr.mpbsapp.vo;

import com.zephyr.mpbsapp.entity.SidebarMenuEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SidebarMenuVO {
    private Long id;
    private String name;
    private String path;
    private String icon;
    private List<SidebarMenuVO> children = new ArrayList<>();
    private Long parentId;

    public SidebarMenuVO(SidebarMenuEntity entity) {
        // 推荐用Spring的BeanUtils，自动映射所有同名属性
        org.springframework.beans.BeanUtils.copyProperties(entity, this);
        // children 已经初始化，无需重复赋值
    }
}
