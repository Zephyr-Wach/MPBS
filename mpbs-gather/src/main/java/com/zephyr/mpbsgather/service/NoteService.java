package com.zephyr.mpbsgather.service;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.entity.NoteCollectionEntity;

/**
 * 笔记服务接口
 * 提供对单个笔记（Note）的基本操作
 */
public interface NoteService {

    /**
     * 添加笔记
     *
     * @param userId     用户 ID，用于标识当前操作用户
     * @param noteEntity 笔记实体对象，包含笔记的标题、内容、所属合集等信息
     * @return           返回操作结果（成功、失败或异常信息）
     */
    Result addNote(String userId, NoteCollectionEntity noteEntity);

    /**
     * 更新笔记
     *
     * @param userId     用户 ID，用于权限校验和归属验证
     * @param noteId     要更新的笔记 ID
     * @param noteEntity 更新后的笔记实体对象
     * @return           返回操作结果
     */
    Result updateNote(String userId, String noteId, NoteCollectionEntity noteEntity);

    /**
     * 删除笔记
     *
     * @param userId 用户 ID，用于权限校验
     * @param noteId 要删除的笔记 ID
     * @return       返回操作结果
     */
    Result delNote(String userId, String noteId);

    /**
     * 获取笔记
     * @param noteId 笔记ID
     * @return 笔记详情或404错误
     */
    Result getNote(String noteId);
}
