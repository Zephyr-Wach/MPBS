package com.zephyr.mpbsblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.service.BlogPostService;
import com.zephyr.mpbscommon.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/blog")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @PostMapping("/post")
    public Result postBlog(@RequestBody BlogPostEntity blogPost ,  Authentication authentication) {
        blogPost.setAuthorId(authentication.getPrincipal().toString());
        return blogPostService.postBlog(blogPost);
    }


    @GetMapping("/getBlogList")
    public Result getBlogList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        IPage<BlogPostEntity> pageResult = blogPostService.getBlogList(pageNum, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/detail/{id}")
    public Result getBlogDetail(@PathVariable String id) {
        BlogPostEntity blog = blogPostService.getBlogDetail(id);
        if (blog == null) {
            return Result.failure(404,"文章不存在或未发布");
        }
        return Result.success(blog);
    }

    // 修改博客
    @PutMapping("/update/{id}")
    public Result<?> updateBlog(@PathVariable String id, @RequestBody BlogPostEntity blogPost) {
        blogPost.setId(id);
        blogPost.setUpdatedAt(new Date());
        boolean updated = blogPostService.updateById(blogPost);
        if (updated) {
            return Result.success("修改成功");
        } else {
            return Result.failure(400,"修改失败");
        }
    }

    // 删除博客
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteBlog(@PathVariable String id) {
        boolean removed = blogPostService.removeById(id);
        if (removed) {
            return Result.success("删除成功");
        } else {
            return Result.failure(400,"删除失败");
        }
    }

}
