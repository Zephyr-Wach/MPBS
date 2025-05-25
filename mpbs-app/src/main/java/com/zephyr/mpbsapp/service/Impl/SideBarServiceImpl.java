package com.zephyr.mpbsapp.service.Impl;

import com.zephyr.mpbsapp.vo.SidebarMenuVO;
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
    public List<SidebarMenuVO> getMenuByRole(String role) {
        // 查询数据库中所有菜单（此处根据权限做过滤）
        List<SidebarMenuEntity> allMenus = sidebarMenuMapper.selectMenusByRole(role);

        List<SidebarMenuVO> voList = allMenus.stream()
                .map(SidebarMenuVO::new)
                .collect(Collectors.toList());

        Map<Long, SidebarMenuVO> voMap = voList.stream()
                .collect(Collectors.toMap(SidebarMenuVO::getId, vo -> vo));

        List<SidebarMenuVO> rootMenus = new ArrayList<>();
        for (SidebarMenuVO vo : voList) {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                rootMenus.add(vo); // 顶级菜单
            } else {
                SidebarMenuVO parent = voMap.get(vo.getParentId());
                if (parent != null) {
                    parent.getChildren().add(vo);
                }
            }
        }

        return rootMenus;
    }
}
