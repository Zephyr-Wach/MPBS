package com.zephyr.mpbsapp.controller;

import com.zephyr.mpbsapp.vo.SidebarMenuVO;
import com.zephyr.mpbsapp.service.SideBarService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
public class SideBarController {

    @Autowired
    private SideBarService sideBarMenuService;

    /**
     * 根据当前登录用户角色获取侧边栏菜单列表
     *
     * @param authentication Spring Security认证信息，包含用户权限
     * @return 侧边栏菜单列表，若无权限则返回默认菜单或空菜单
     */
    @GetMapping("/getSideBarList")
    public List<SidebarMenuVO> getSideBarList(Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                sideBarMenuService.getMenuByRole(null) :
                sideBarMenuService.getMenuByRole(authentication.getAuthorities().iterator().next().getAuthority());
    }
}
