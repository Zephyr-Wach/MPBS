package com.zephyr.mpbsfiles.mapper;

import com.zephyr.mpbsfiles.dto.ShareLink;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ShareMapper {
    @Insert("insert into temporary_links(id, file_id, expires_at, created_at, url) values(#{id}, #{fileId}, #{expiresAt}, #{createdAt}, #{url})")
    void insert(ShareLink share);

    @Select("select * from temporary_links where id = #{id}")
    ShareLink getShareById(String id);
}
