package com.zephyr.mpbsgather.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.entity.NoteCollectionEntity;
import com.zephyr.mpbsgather.vo.GatherVO;

/**
 * 笔记合集服务接口
 * 定义了笔记合集（Collection）的基本操作
 */
public interface CollectionService {

    /**
     * 创建新的笔记合集
     *
     * @param userId        用户 ID，用于标识当前操作用户
     * @param gatherEntity  笔记合集实体，包含合集的基本信息
     * @return              返回操作结果（成功或失败等信息）
     */
    Result createCollection(String userId, NoteCollectionEntity gatherEntity);

    /**
     * 更新已有的笔记合集
     *
     * @param userId     用户 ID，用于权限校验和归属验证
     * @param gatherId   合集 ID，指定要更新的合集
     * @param convert    更新后的合集实体数据
     * @return           返回操作结果
     */
    Result updateCollection(String userId, String gatherId, NoteCollectionEntity convert);

    /**
     * 删除指定的笔记合集
     *
     * @param userId    用户 ID，用于权限校验
     * @param gatherId  要删除的合集 ID
     * @return          返回操作结果
     */
    Result delCollection(String userId, String gatherId);

    /**
     * 模糊搜索合集
     * @param keyword  搜索关键字
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return
     */
    IPage<GatherVO> fuzzySearch(String keyword, int pageNum, int pageSize);

    /**
     * 列出所有的笔记合集
     * @return 返回所有的笔记合集
     */
    Result listCollection();
}
