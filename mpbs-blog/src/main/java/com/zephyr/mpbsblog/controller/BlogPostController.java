package com.zephyr.mpbsblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.service.BlogPostService;
import com.zephyr.mpbscommon.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

}
