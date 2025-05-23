package com.zephyr.mpbsblog.controller;

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

}
