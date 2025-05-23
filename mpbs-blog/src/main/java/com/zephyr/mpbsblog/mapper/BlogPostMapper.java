package com.zephyr.mpbsblog.mapper;

import com.zephyr.mpbsblog.entity.BlogPostEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogPostMapper {
    int insert(BlogPostEntity blogPost);
}