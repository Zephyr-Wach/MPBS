package com.zephyr.mpbsgather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.mpbsgather.entity.NoteCollectionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface NoteCollectionMapper extends BaseMapper<NoteCollectionEntity> {

    int insert(NoteCollectionEntity entity);

    String queryAuthorIdByGatherId(@Param("gatherId") String gatherId);

    int updateById(NoteCollectionEntity entity);

    int deleteById(@Param("id") String id);

    List<NoteCollectionEntity> selectAllTitleAndDescription();
}
