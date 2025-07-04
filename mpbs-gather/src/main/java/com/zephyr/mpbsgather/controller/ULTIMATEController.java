package com.zephyr.mpbsgather.controller;

import com.zephyr.mpbsgather.dto.CreateGatherDTO;
import com.zephyr.mpbsgather.dto.CreateNoteDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ULTIMATE")
public class ULTIMATEController {

    /**
     * 创建集合
     * @param createGatherDTO 创建集合的参数
     * @return 创建结果
     */
    @RequestMapping("/createCollection")
    public String createCollection(@RequestBody CreateGatherDTO createGatherDTO) {
        return "创建成功";
    }

    /**
     * 修改集合信息
     * @param gatherId 集合ID
     * @param updateGatherDTO 修改集合的参数
     * @return 修改结果
     */
    @RequestMapping("/updateCollection")
    public String updateCollection(String gatherId, @RequestBody CreateGatherDTO updateGatherDTO) {
        return "修改成功";
    }

    /**
     * 删除集合
     * @param gatherId 集合ID
     * @return 删除结果
     */
    @RequestMapping("/deleteCollection")
    public String deleteCollection(String gatherId) {
        return "删除成功";
    }

    /**
     * 添加笔记
     * @param createNoteDTO 添加笔记的参数
     * @return 添加结果
     */
    @RequestMapping("/addNote")
    public String addNote(@RequestBody CreateNoteDTO createNoteDTO) {
        return "添加成功";
    }

    /**
     * 修改笔记信息
     * @param noteId 笔记ID
     * @param updateNoteDTO 修改笔记的参数
     * @return 修改结果
     */
    @RequestMapping("/updateNote")
    public String updateNote(String noteId, @RequestBody CreateNoteDTO updateNoteDTO) {
        return "修改成功";
    }

    /**
     * 删除笔记
     * @param noteId 笔记ID
     * @return 删除结果
     */
    @RequestMapping("/deleteNote")
    public String deleteNote(String noteId) {
        return "删除成功";
    }

}
