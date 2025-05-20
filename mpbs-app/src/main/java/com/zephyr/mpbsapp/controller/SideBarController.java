package com.zephyr.mpbsapp.controller;

import com.zephyr.mpbsapp.dto.SidebarMenuDto;
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

    @GetMapping("/getSideBarList")
    public List<SidebarMenuDto> getSideBarList(Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                sideBarMenuService.getMenuByRole(null) :
                sideBarMenuService.getMenuByRole(authentication.getAuthorities().iterator().next().getAuthority());
    }
}
