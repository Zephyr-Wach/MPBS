package com.zephyr.mpbsblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbscommon.utils.Result;

public interface BlogPostService {
    Result postBlog(BlogPostEntity BlogPostEntity);

    IPage<BlogPostEntity> getBlogList(int pageNum, int pageSize) ;
}
