package com.zephyr.mpbsblog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

@Mapper
public interface BlogPostMapper extends BaseMapper<BlogPostEntity>{
    int insert(BlogPostEntity blogPost);

    @Select("SELECT id, title, content_md, content_html, cover_url, author_id, status, created_at, updated_at " +
            "FROM blog_post WHERE id = #{id} AND status = 'published'")
    BlogPostEntity selectByIdPublished(String id);
}