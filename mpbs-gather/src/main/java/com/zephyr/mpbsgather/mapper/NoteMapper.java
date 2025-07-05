package com.zephyr.mpbsgather.mapper;

import com.zephyr.mpbsgather.entity.NoteEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface NoteMapper {
    
    int insert(NoteEntity noteEntity);
    
    String queryAuthorIdByNoteId(@Param("noteId") String noteId);
    
    int updateById(NoteEntity noteEntity);
    
    int deleteById(@Param("id") String id);

    NoteEntity getAllById(String noteId);
}
