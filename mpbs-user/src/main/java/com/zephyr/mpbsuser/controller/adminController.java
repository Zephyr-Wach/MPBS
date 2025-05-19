package com.zephyr.mpbsuser.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class adminController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
