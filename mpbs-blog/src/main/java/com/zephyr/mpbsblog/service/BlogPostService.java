package com.zephyr.mpbsblog.service;

import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbscommon.utils.Result;

public interface BlogPostService {
    Result postBlog(BlogPostEntity BlogPostEntity);
}
