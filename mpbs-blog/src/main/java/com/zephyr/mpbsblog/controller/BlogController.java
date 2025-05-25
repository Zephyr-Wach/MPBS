package com.zephyr.mpbsblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.entity.Comment;
import com.zephyr.mpbsblog.service.BlogPostService;
import com.zephyr.mpbsblog.service.CommentService;
import com.zephyr.mpbsblog.vo.CommentVO;
import com.zephyr.mpbscommon.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private CommentService commentService;

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

    /**
     * 新增评论
     */
    @PostMapping("/addComment")
    public Result<?> addComment(@RequestBody Comment comment, Authentication authentication) {
        // 获取当前用户ID（假设你使用的是用户ID作为 principal）
        comment.setUserId(Long.valueOf(authentication.getPrincipal().toString()));
        try {
            commentService.addComment(comment);
            return Result.success("评论成功");
        } catch (RuntimeException e) {
            return Result.failure(400, e.getMessage());
        }
    }

    /**
     * 获取指定博客的所有评论（按时间升序）
     */
    @GetMapping("/getCommentsByPost/{postId}")
    public Result<List<CommentVO>> getCommentTree(@PathVariable Long postId) {
        List<CommentVO> commentTree = commentService.getCommentTreeByPostId(postId);
        return Result.success(commentTree);
    }

    @DeleteMapping("/comment/delete/{id}")
    public Result<?> deleteComment(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());

        // 获取权限，例如 ROLE_ULTIMATE
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ULTIMATE"));

        boolean success = commentService.deleteCommentById(id, userId, isAdmin);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.failure(403, "无权限删除该评论或评论不存在");
        }
    }


}
