package com.zephyr.mpbsgather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.mpbsgather.entity.NoteCollectionEntity;
import com.zephyr.mpbsgather.entity.NoteEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface NoteMapper extends BaseMapper<NoteEntity> {
    
    int insert(NoteCollectionEntity noteEntity);
    
    String queryAuthorIdByNoteId(@Param("noteId") String noteId);
    
    int updateById(NoteCollectionEntity noteEntity);
    
    int deleteById(@Param("id") String id);

    NoteEntity getAllById(String noteId);
}
