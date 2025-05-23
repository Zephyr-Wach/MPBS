package com.zephyr.mpbsblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbscommon.utils.Result;

public interface BlogPostService extends IService<BlogPostEntity> {
    Result postBlog(BlogPostEntity BlogPostEntity);

    IPage<BlogPostEntity> getBlogList(int pageNum, int pageSize) ;

    BlogPostEntity getBlogDetail(String id);
}
