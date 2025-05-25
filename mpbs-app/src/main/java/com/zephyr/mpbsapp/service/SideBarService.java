package com.zephyr.mpbsapp.service;

import com.zephyr.mpbsapp.vo.SidebarMenuVO;

import java.util.List;

public interface SideBarService {
    /**
     * 根据角色获取侧边栏菜单树
     *
     * @param role 用户角色标识
     * @return 该角色对应的侧边栏菜单DTO列表，通常是树形结构
     */
    List<SidebarMenuVO> getMenuByRole(String role);
}
