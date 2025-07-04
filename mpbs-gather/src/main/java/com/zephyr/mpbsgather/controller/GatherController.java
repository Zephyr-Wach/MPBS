package com.zephyr.mpbsgather.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/gather")
public class GatherController {

    /**
     * 模糊查询集合
     * @param keyword 关键字
     * @return 查询结果
     */
    @RequestMapping("/search")
    public String searchGather(String keyword) {
        return "模糊查询集合";
    }

    /**
     * 获取集合列表
     * @return 集合列表
     */
    @RequestMapping("/list")
    public String listGather() {
        return "获取集合列表";
    }

    /**
     * 获取指定note的内容
     * @param noteId note的ID
     * @return 查询结果
     */
    @RequestMapping("/getNote")
    public String getNote(String noteId) {
        return "获取指定note的内容";
    }
}
