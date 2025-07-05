package com.zephyr.mpbsgather.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.service.CollectionService;
import com.zephyr.mpbsgather.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/gather")
public class GatherController {

    @Autowired
    private CollectionService gatherService;

    @Autowired
    private NoteService noteService;

    /**
     * 模糊查询集合
     * @param keyword 关键字
     * @return 查询结果
     */
    @RequestMapping("/search")
    public Result searchGather(@RequestParam String keyword,
                               @RequestParam(defaultValue = "1") int pageNum,
                               @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success( gatherService.fuzzySearch(keyword,pageNum, pageSize) );
    }

    /**
     * 获取集合列表
     * @return 集合列表
     */
    @RequestMapping("/list")
    public Result listGather() {return gatherService.listCollection();}

    /**
     * 获取指定note的内容
     * @param noteId note的ID
     * @return 查询结果
     */
    @RequestMapping("/getNote")
    public Result getNote(@RequestParam String noteId) {return noteService.getNote(noteId);}
}
