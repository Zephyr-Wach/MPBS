package com.zephyr.mpbsblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogPostMapper extends BaseMapper<BlogPostEntity> {

    /**
     * 插入一条博客文章记录
     *
     * @param blogPost 博客文章实体
     * @return 受影响的行数
     */
    int insert(BlogPostEntity blogPost);

    /**
     * 根据ID查询已发布的博客文章
     *
     * @param id 博客文章ID
     * @return 博客文章实体，如果未发布或不存在则返回null
     */
    BlogPostEntity selectByIdPublished(String id);
}
