package com.zephyr.mpbsuser.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr")
public class userController {

    /**
     * login
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "login";
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
