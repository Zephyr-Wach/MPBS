package com.zephyr.mpbsblog.service.Impl;

import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.mapper.BlogPostMapper;
import com.zephyr.mpbsblog.service.BlogPostService;
import com.zephyr.mpbscommon.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
