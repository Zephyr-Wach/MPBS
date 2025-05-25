package com.zephyr.mpbsblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.vo.BlogVO;
import com.zephyr.mpbscommon.utils.Result;

/**
 * 博客文章业务接口
 * 提供博客文章的发布、列表查询和详情获取功能
 */
public interface BlogPostService extends IService<BlogPostEntity> {

    /**
     * 发布一篇博客文章
     *
     * @param blogPostEntity 博客文章实体
     * @return 处理结果，包含成功或失败信息
     */
    Result postBlog(BlogPostEntity blogPostEntity);

    /**
     * 分页获取博客文章列表
     *
     * @param pageNum  当前页码，从1开始
     * @param pageSize 每页记录数
     * @return 分页结果对象，包含博客文章列表
     */
    IPage<BlogVO> getBlogList(int pageNum, int pageSize);

    /**
     * 根据ID获取博客文章详情
     *
     * @param id 博客文章ID
     * @return 博客文章实体，若不存在则返回null
     */
    BlogPostEntity getBlogDetail(String id);
}

