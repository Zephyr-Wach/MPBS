package com.zephyr.mpbsgather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.mpbsgather.entity.RelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoteCollectionRelationMapper extends BaseMapper<RelationEntity> {
    String queryGatherIdByNoteId(String noteId);

    int countNoteIdByGatherId(String gatherId);

    int queryIndexByNoteId(String noteId);

    List<String> queryNoteIdByGatherId(String gatherId);

    int batchUpdateNoteOrder(@Param("gatherId") Long gatherId,
                             @Param("orderMap") Map<Long, Integer> orderMap);

}
