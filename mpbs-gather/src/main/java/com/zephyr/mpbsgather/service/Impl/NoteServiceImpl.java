package com.zephyr.mpbsgather.service.Impl;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.entity.NoteCollectionEntity;
import com.zephyr.mpbsgather.service.NoteService;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {
    @Override
    public Result addNote(String userId, NoteCollectionEntity noteEntity) {
        return null;
    }

    @Override
    public Result updateNote(String userId, String noteId, NoteCollectionEntity noteEntity) {
        return null;
    }

    @Override
    public Result delNote(String userId, String noteId) {
        return null;
    }

    @Override
    public Result getNote(String noteId) {
        return null;
    }
}
