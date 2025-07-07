package com.zephyr.mpbsgather.service.Impl;

import com.zephyr.mpbscommon.utils.BeanConvertUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.entity.NoteEntity;
import com.zephyr.mpbsgather.mapper.NoteMapper;
import com.zephyr.mpbsgather.service.NoteService;
import com.zephyr.mpbsgather.vo.NoteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteMapper noteMapper;
    @Override
    public Result addNote(String userId, NoteEntity noteEntity) {
        noteEntity.setCreatedAt(LocalDateTime.now());
        int inserted = noteMapper.insert(noteEntity);
        return inserted > 0 ? Result.success(noteEntity.getId()) : Result.failure(400,"创建失败");
    }

    @Override
    public Result updateNote(String userId, String noteId, NoteEntity noteEntity) {
        NoteEntity localEntity = noteMapper.getAllById(noteId);
        if (Objects.equals(noteEntity.getContentMd(), "notUpdate")){
            noteEntity.setContentMd(localEntity.getContentMd());
        }
        if ((Objects.equals(noteEntity.getIsPublic(), "2"))){
            noteEntity.setIsPublic(localEntity.getIsPublic());
        }
        if (Objects.equals(noteEntity.getTitle(), "notUpdate")){
            noteEntity.setTitle(localEntity.getTitle());
        }
        noteEntity.setId(noteId);
        noteEntity.setUpdatedAt(LocalDateTime.now());
        return noteMapper.updateById(noteEntity) > 0 ? Result.success("更新成功") : Result.failure(400,"更新失败");
    }

    @Override
    public Result delNote(String userId, String noteId) {
        return noteMapper.deleteById(noteId) > 0 ? Result.success("删除成功") : Result.failure(400,"删除失败");
    }

    @Override
    public Result getNote(String noteId) {
        NoteEntity noteEntity = noteMapper.getAllById(noteId);
        return noteEntity != null ? Result.success(BeanConvertUtil.convert(noteEntity, NoteVO.class)) : Result.failure(400,"无此笔记");
    }
}
