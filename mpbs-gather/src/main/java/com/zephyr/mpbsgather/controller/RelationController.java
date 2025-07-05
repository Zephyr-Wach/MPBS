package com.zephyr.mpbsgather.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/relation")
public class RelationController {
    @Autowired
    private RelationService relationService;

    /**
     * 查询指定笔记的关联关系
     * @param noteId
     * @return 查询结果
     */
    @RequestMapping("/queryNoteRelation")
    public Result queryNoteRelation(@RequestParam String noteId) {
        return relationService.queryNoteRelation(noteId);
    }

    /**
     * 查询指合集下的所有笔记
     * @param gatherId
     * @return 查询结果
     */
    @RequestMapping("/queryGatherNotes")
    public Result queryGatherNotes(@RequestParam String gatherId) {
        return relationService.queryGatherNotes(gatherId);
    }
}
