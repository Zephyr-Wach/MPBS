package com.zephyr.mpbsblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.entity.Comment;
import com.zephyr.mpbsblog.service.BlogPostService;
import com.zephyr.mpbsblog.service.CommentService;
import com.zephyr.mpbsblog.vo.BlogVO;
import com.zephyr.mpbsblog.vo.CommentVO;
import com.zephyr.mpbscommon.utils.BeanConvertUtil;
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

    /**
     * 发布博客文章
     * @param blogPost 博客实体
     * @param authentication 认证信息，获取当前用户ID作为作者
     * @return 统一结果对象，包含成功或失败信息
     */
    @PostMapping("/post")
    public Result postBlog(@RequestBody BlogPostEntity blogPost, Authentication authentication) {
        blogPost.setAuthorId(authentication.getPrincipal().toString());
        return blogPostService.postBlog(blogPost);
    }

    /**
     * 获取博客列表，支持分页
     * @param pageNum 页码，默认1
     * @param pageSize 每页条数，默认10
     * @return 分页博客列表
     */
    @GetMapping("/getBlogList")
    public Result getBlogList(@RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        IPage<BlogVO> pageResult = blogPostService.getBlogList(pageNum, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 获取指定ID的博客详情
     * @param id 博客ID
     * @return 博客详情或404错误
     */
    @GetMapping("/detail/{id}")
    public Result getBlogDetail(@PathVariable String id) {
        BlogPostEntity blog = blogPostService.getBlogDetail(id);
        if (blog == null) {
            return Result.failure(404, "文章不存在或未发布");
        }
        return Result.success(BeanConvertUtil.convert(blog,BlogVO.class));
    }

    /**
     * 修改指定ID的博客文章
     * @param id 博客ID
     * @param blogPost 新的博客数据
     * @return 操作结果，成功或失败信息
     */
    @PutMapping("/update/{id}")
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
    public Result<?> deleteBlog(@PathVariable String id) {
        boolean removed = blogPostService.removeById(id);
        if (removed) {
            return Result.success("删除成功");
        } else {
            return Result.failure(400, "删除失败");
        }
    }

    /**
     * 新增评论
     * @param comment 评论实体
     * @param authentication 认证信息，获取当前用户ID作为评论者
     * @return 操作结果，成功或失败信息
     */
    @PostMapping("/addComment")
    public Result<?> addComment(@RequestBody Comment comment, Authentication authentication) {
        comment.setUserId(Long.valueOf(authentication.getPrincipal().toString()));
        try {
            commentService.addComment(comment);
            return Result.success("评论成功");
        } catch (RuntimeException e) {
            return Result.failure(400, e.getMessage());
        }
    }

    /**
     * 获取指定博客文章的所有评论，树形结构
     * @param postId 博客文章ID
     * @return 评论树形列表
     */
    @GetMapping("/getCommentsByPost/{postId}")
    public Result<List<CommentVO>> getCommentTree(@PathVariable Long postId) {
        List<CommentVO> commentTree = commentService.getCommentTreeByPostId(postId);
        return Result.success(commentTree);
    }

    /**
     * 删除指定ID的评论
     * @param id 评论ID
     * @param authentication 认证信息，获取当前用户ID和权限
     * @return 操作结果，成功或权限不足失败
     */
    @DeleteMapping("/comment/delete/{id}")
    public Result<?> deleteComment(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
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
