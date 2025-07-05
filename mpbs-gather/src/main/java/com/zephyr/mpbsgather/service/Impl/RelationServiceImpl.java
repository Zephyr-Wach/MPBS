package com.zephyr.mpbsgather.service.Impl;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.dto.GatherOrderUpdateDTO;
import com.zephyr.mpbsgather.mapper.NoteCollectionRelationMapper;
import com.zephyr.mpbsgather.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RelationServiceImpl implements RelationService {
    @Autowired
    private NoteCollectionRelationMapper relationMapper;
    @Override
    public Result queryNoteRelation(String noteId) {
        Map<String, Object> data = new HashMap<>();
        String gatherId = relationMapper.queryGatherIdByNoteId(noteId);
        data.put("gatherId", gatherId);
        data.put("total",relationMapper.countNoteIdByGatherId(gatherId));
        data.put("index", relationMapper.queryIndexByNoteId(noteId));
        return Result.success(data);
    }

    @Override
    public Result queryGatherNotes(String gatherId) {
        return Result.success( relationMapper.queryNoteIdByGatherId(gatherId) );
    }

    @Override
    public Result updateGatherNoteOrder(GatherOrderUpdateDTO gatherOrderUpdateDTO) {
        Long gatherId = Long.valueOf(gatherOrderUpdateDTO.getGatherId());
        Map<Long, Integer> orderMap = gatherOrderUpdateDTO.getOrder();
        if (orderMap == null || orderMap.isEmpty()) {
            return Result.failure(400, "排序数据为空");
        }

        int updatedCount = relationMapper.batchUpdateNoteOrder(gatherId, orderMap);

        if (updatedCount > 0) {
            return Result.success("排序全部更新成功");
        } else {
            return Result.failure(400, "排序更新失败");
        }
    }

}
