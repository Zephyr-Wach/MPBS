package com.zephyr.mpbsgather.service;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.dto.GatherNoteAddDTO;
import com.zephyr.mpbsgather.dto.GatherOrderUpdateDTO;

/**
 * 笔记合集关系服务接口
 */
public interface RelationService {

    /**
     * 查询当前笔记所在合集及合集下的位置信息
     */
    public Result queryNoteRelation(String noteId);

    /**
     * 查询当前合集下的所有笔记
     */
    public Result queryGatherNotes(String gatherId);

    /**
     * 更新集合下笔记顺序
     */
    public Result updateGatherNoteOrder(GatherOrderUpdateDTO gatherOrderUpdateDTO);

    /**
     * 添加笔记到合集
      */
    Result addNoteToGather(GatherNoteAddDTO dto);
}
