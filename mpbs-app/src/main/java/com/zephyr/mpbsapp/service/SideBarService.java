package com.zephyr.mpbsapp.service;

import com.zephyr.mpbsapp.dto.SidebarMenuDto;

import java.util.List;

public interface SideBarService {
    /**
     * get  sidebar menu tree by role
     * @param role
     * @return
     */
    List<SidebarMenuDto> getMenuByRole(String role);
}
