package com.zephyr.mpbsgather.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zephyr.mpbscommon.utils.BeanConvertUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsgather.entity.NoteCollectionEntity;
import com.zephyr.mpbsgather.mapper.NoteCollectionMapper;
import com.zephyr.mpbsgather.service.CollectionService;
import com.zephyr.mpbsgather.vo.GatherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    private NoteCollectionMapper gatherMapper;

    @Override
    public Result createCollection(String userId, NoteCollectionEntity gatherEntity) {
        gatherEntity.setAuthorId(userId);
        gatherEntity.setCreatedAt(LocalDateTime.now());
        int inserted = gatherMapper.insert(gatherEntity);
        return inserted > 0 ? Result.success("创建成功") : Result.failure(400,"创建失败");
    }

    @Override
    public Result updateCollection(String userId, String gatherId, NoteCollectionEntity convert) {
        convert.setId(gatherId);
        convert.setUpdatedAt(LocalDateTime.now());
        return userId.equals(gatherMapper.queryAuthorIdByGatherId(gatherId))?
                gatherMapper.updateById(convert) > 0 ? Result.success("更新成功") : Result.failure(400,"更新失败") :
                Result.failure(403,"无权限");
    }

    @Override
    public Result delCollection(String userId, String gatherId) {
        return userId.equals(gatherMapper.queryAuthorIdByGatherId(gatherId))?
                gatherMapper.deleteById(gatherId) > 0 ? Result.success("删除成功") : Result.failure(400,"删除失败") :
                Result.failure(403,"无权限");
    }

    @Override
    public IPage<GatherVO> fuzzySearch(String keyword, int pageNum, int pageSize) {
        // 1. 构造分页参数
        Page<NoteCollectionEntity> page = new Page<>(pageNum, pageSize);

        // 2. 构造模糊查询条件（搜索 title 和 description）
        QueryWrapper<NoteCollectionEntity> query = new QueryWrapper<>();
        query.eq("is_public", "1")  // 只查公开的
                .and(wrapper -> wrapper
                        .like("title", keyword)
                        .or()
                        .like("description", keyword))
                .orderByDesc("created_at");

        // 3. 分页查询
        IPage<NoteCollectionEntity> entityPage = gatherMapper.selectPage(page, query);

        // 4. 实体转VO
        List<GatherVO> voList = BeanConvertUtil.convertList(entityPage.getRecords(), GatherVO.class);

        // 5. 构造分页VO结果
        Page<GatherVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public Result listCollection() {
        List<NoteCollectionEntity> list = gatherMapper.selectAllTitleAndDescription();
        return list != null ? Result.success(list) : Result.failure(500, "查询失败");
    }
}
