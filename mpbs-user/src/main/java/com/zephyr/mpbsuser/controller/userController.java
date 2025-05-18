package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbsuser.service.userService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zephyr.mpbscommon.utils.Result;




@Controller
@RequestMapping("/usr")
public class userController {

    private userService UserService;

    /**
     * login
     * @return
     */
    @RequestMapping("/login")
    public Result login(){

        return UserService.login();
    }

    /**
     * register
     * @return
     */
    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    /**
     * getUserInfo
     */
    @RequestMapping("/getUserInfo")
    public String getUserInfo(){
        return "getUserInfo";
    }
}
