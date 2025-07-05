package com.zephyr.mpbsgather.controller;

import com.zephyr.mpbscommon.utils.BeanConvertUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.dto.GatherDTO;
import com.zephyr.mpbsgather.dto.GatherOrderUpdateDTO;
import com.zephyr.mpbsgather.dto.NoteDTO;
import com.zephyr.mpbsgather.entity.NoteCollectionEntity;
import com.zephyr.mpbsgather.entity.NoteEntity;
import com.zephyr.mpbsgather.service.CollectionService;
import com.zephyr.mpbsgather.service.NoteService;
import com.zephyr.mpbsgather.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ULTIMATE/gather")
public class GatherULTIMATEController {
    @Autowired
    private CollectionService gatherService;

    @Autowired
    private NoteService noteService;
    @Autowired
    private RelationService relationService;
    @PostMapping("/hello")
    public Result hello() {
        return Result.success("hello");
    }

    /**
     * 创建集合
     * @param gatherDTO 创建集合的参数
     * @param authentication 认证信息
     * @return 创建结果
     */
    @PostMapping("/createCollection")
    public Result createCollection(@RequestBody GatherDTO gatherDTO, Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                gatherService.createCollection(authentication.getPrincipal().toString(), BeanConvertUtil.convert(gatherDTO, NoteCollectionEntity.class));
    }

    /**
     * 修改集合信息
     * @param gatherId 集合ID
     * @param gatherDTO 修改集合的参数
     * @param authentication 认证信息
     * @return 修改结果
     */
    @PostMapping("/updateCollection")
    public Result updateCollection(@RequestParam String gatherId, @RequestBody GatherDTO gatherDTO, Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                gatherService.updateCollection(authentication.getPrincipal().toString(), gatherId, BeanConvertUtil.convert(gatherDTO, NoteCollectionEntity.class));
    }

    /**
     * 删除集合
     * @param gatherId 集合ID
     * @param authentication 认证信息
     * @return 删除结果
     */
    @RequestMapping("/deleteCollection")
    public Result deleteCollection(@RequestParam String gatherId, Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                gatherService.delCollection(authentication.getPrincipal().toString(), gatherId);
    }

    /**
     * 添加笔记
     * @param noteDTO 添加笔记的参数
     * @param authentication 认证信息
     * @return 添加结果
     */
    @PostMapping("/addNote")
    public Result addNote(@RequestBody NoteDTO noteDTO, Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                noteService.addNote(authentication.getPrincipal().toString(), BeanConvertUtil.convert(noteDTO, NoteEntity.class));
    }

    /**
     * 修改笔记信息
     * @param noteId 笔记ID
     * @param noteDTO 修改笔记的参数
     * @param authentication 认证信息
     * @return 修改结果
     */
    @PostMapping("/updateNote")
    public Result updateNote(@RequestParam String noteId, @RequestBody NoteDTO noteDTO, Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                noteService.updateNote(authentication.getPrincipal().toString(), noteId, BeanConvertUtil.convert(noteDTO, NoteEntity.class));
    }

    /**
     * 删除笔记
     * @param noteId 笔记ID
     * @param authentication 认证信息
     * @return 删除结果
     */
    @RequestMapping("/deleteNote")
    public Result deleteNote(@RequestParam String noteId, Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                noteService.delNote(authentication.getPrincipal().toString(), noteId);
    }

    /**
     * 更新合集中笔记排序
     * @param dto 合集ID和排序信息
     * @return result
     */
    @PostMapping("/updateOrder")
    public Result updateOrder(@RequestBody GatherOrderUpdateDTO dto) {
        return relationService.updateGatherNoteOrder(dto);
    }
}
