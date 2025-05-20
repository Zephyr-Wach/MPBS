package com.zephyr.mpbsapp.service.Impl;

import com.zephyr.mpbsapp.dto.SidebarMenuDto;
import com.zephyr.mpbsapp.entity.SidebarMenuEntity;
import com.zephyr.mpbsapp.mapper.SidebarMenuMapper;
import com.zephyr.mpbsapp.service.SideBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SideBarServiceImpl implements SideBarService {

    @Autowired
    private SidebarMenuMapper sidebarMenuMapper;

    @Override
    public List<SidebarMenuDto> getMenuByRole(String role) {
        // 查询数据库中所有菜单（此处根据权限做过滤）
        List<SidebarMenuEntity> allMenus = sidebarMenuMapper.selectMenusByRole(role);

        // 1. 转换为 DTO 列表
        List<SidebarMenuDto> dtoList = allMenus.stream()
                .map(SidebarMenuDto::new)
                .collect(Collectors.toList());

        // 2. 构建 ID -> DTO 映射
        Map<Long, SidebarMenuDto> dtoMap = dtoList.stream()
                .collect(Collectors.toMap(SidebarMenuDto::getId, dto -> dto));

        // 3. 构建树结构
        List<SidebarMenuDto> rootMenus = new ArrayList<>();
        for (SidebarMenuDto dto : dtoList) {
            if (dto.getParentId() == null || dto.getParentId() == 0) {
                rootMenus.add(dto); // 顶级菜单
            } else {
                SidebarMenuDto parent = dtoMap.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        return rootMenus;
    }
}
