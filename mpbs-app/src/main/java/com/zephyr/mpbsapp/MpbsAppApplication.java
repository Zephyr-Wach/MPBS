package com.zephyr.mpbsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.zephyr.mpbsapp",
        "com.zephyr.mpbsauth",
        "com.zephyr.mpbsblog",
        "com.zephyr.mpbscommon",
        "com.zephyr.mpbsfiles",
        "com.zephyr.mpbssecurity",
        "com.zephyr.mpbsuser",
        "com.zephyr.mpbsgather"
})
public class MpbsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpbsAppApplication.class, args);
    }

}
