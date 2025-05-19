package com.zephyr.mpbsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.zephyr.mpbsapp",
        "com.zephyr.mpbsauth",
        "com.zephyr.mbpsblog",
        "com.zephyr.mpbscommon",
        "com.zephyr.mpbsfiles",
        "com.zephyr.mpbssecurity",
        "com.zephyr.mpbsuser"
})
public class MpbsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpbsAppApplication.class, args);
    }

}
