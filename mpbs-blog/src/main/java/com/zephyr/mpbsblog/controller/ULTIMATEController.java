package com.zephyr.mpbsblog.controller;

import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.service.BlogPostService;
import com.zephyr.mpbscommon.annotation.LogOperation;
import com.zephyr.mpbscommon.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/ULTIMATE/blog")
public class ULTIMATEController {
    @Autowired
    private BlogPostService blogPostService;

    /**
     * 发布博客文章
     * @param blogPost 博客实体
     * @param authentication 认证信息，获取当前用户ID作为作者
     * @return 统一结果对象，包含成功或失败信息
     */
    @PostMapping("/post")
    @LogOperation(operationType = "发表博客文章")
    public Result postBlog(@RequestBody BlogPostEntity blogPost, Authentication authentication) {
        blogPost.setAuthorId(authentication.getPrincipal().toString());
        return blogPostService.postBlog(blogPost);
    }

    /**
     * 修改指定ID的博客文章
     * @param id 博客ID
     * @param blogPost 新的博客数据
     * @return 操作结果，成功或失败信息
     */
    @PutMapping("/update/{id}")
    @LogOperation(operationType = "修改指定ID的博客文章")
    public Result<?> updateBlog(@PathVariable String id, @RequestBody BlogPostEntity blogPost) {
        blogPost.setId(id);
        blogPost.setUpdatedAt(new Date());
        boolean updated = blogPostService.updateById(blogPost);
        if (updated) {
            return Result.success("修改成功");
        } else {
            return Result.failure(400, "修改失败");
        }
    }

    /**
     * 删除指定ID的博客文章
     * @param id 博客ID
     * @return 操作结果，成功或失败信息
     */
    @DeleteMapping("/delete/{id}")
    @LogOperation(operationType = "删除指定ID的博客文章")
    public Result<?> deleteBlog(@PathVariable String id) {
        boolean removed = blogPostService.removeById(id);
        if (removed) {
            return Result.success("删除成功");
        } else {
            return Result.failure(400, "删除失败");
        }
    }
}
