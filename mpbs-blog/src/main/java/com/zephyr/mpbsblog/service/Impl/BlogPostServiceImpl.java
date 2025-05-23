package com.zephyr.mpbsblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.mapper.BlogPostMapper;
import com.zephyr.mpbsblog.service.BlogPostService;
import com.zephyr.mpbscommon.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    @Autowired
    private BlogPostMapper blogPostMapper;

    @Override
    public Result postBlog(BlogPostEntity BlogPostEntity) {

        return blogPostMapper.insert(BlogPostEntity) > 0 ?
                Result.success() :
                Result.failure(400, "发布失败");
    }

    @Override
    public IPage<BlogPostEntity> getBlogList(int pageNum, int pageSize) {
        Page<BlogPostEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<BlogPostEntity> query = new QueryWrapper<>();
        query.eq("status", "published").orderByDesc("created_at");
        return blogPostMapper.selectPage(page, query);
    }

    @Override
    public BlogPostEntity getBlogDetail(String id) {
        return blogPostMapper.selectByIdPublished(id);
    }

}
