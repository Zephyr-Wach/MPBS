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
@RequestMapping("/sideBar")
public class SideBarController {

    @Autowired
    private SideBarService sideBarMenuService;

    @GetMapping("/getSideBarList")
    public List<SidebarMenuDto> getSideBarList(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return sideBarMenuService.getMenuByRole(role);
    }
}
