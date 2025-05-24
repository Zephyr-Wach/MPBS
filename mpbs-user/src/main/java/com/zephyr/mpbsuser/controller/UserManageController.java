package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    private UserService userService;


}
