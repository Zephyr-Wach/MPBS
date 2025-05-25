package com.zephyr.mpbsfiles.mapper;

import com.zephyr.mpbsfiles.dto.ShareLink;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareMapper {
    /**
     * 插入分享链接信息
     * @param share 分享链接实体
     */
    void insert(ShareLink share);

    /**
     * 根据分享链接ID查询分享链接信息
     * @param id 分享链接ID
     * @return 分享链接实体
     */
    ShareLink getShareById(String id);

}
