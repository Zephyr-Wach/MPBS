package com.zephyr.mpbsblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.mpbsblog.entity.BlogPostEntity;
import com.zephyr.mpbsblog.mapper.BlogPostMapper;
import com.zephyr.mpbsblog.service.BlogPostService;
import com.zephyr.mpbsblog.vo.BlogVO;
import com.zephyr.mpbscommon.utils.BeanConvertUtil;
import com.zephyr.mpbscommon.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogPostServiceImpl extends ServiceImpl<BlogPostMapper, BlogPostEntity> implements BlogPostService {

    @Autowired
    private BlogPostMapper blogPostMapper;

    @Override
    public Result postBlog(BlogPostEntity BlogPostEntity) {

        return blogPostMapper.insert(BlogPostEntity) > 0 ?
                Result.success() :
                Result.failure(400, "发布失败");
    }

    @Override
    public IPage<BlogVO> getBlogList(int pageNum, int pageSize) {
        // 1. 构造分页参数和查询条件
        Page<BlogPostEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<BlogPostEntity> query = new QueryWrapper<>();
        query.eq("status", "published").orderByDesc("created_at");

        // 2. 查询分页实体数据
        IPage<BlogPostEntity> entityPage = blogPostMapper.selectPage(page, query);

        // 3. 转换实体列表为VO列表
        List<BlogVO> voList = BeanConvertUtil.convertList(entityPage.getRecords(), BlogVO.class);

        // 4. 构造新的分页对象，赋值分页信息和VO列表
        Page<BlogVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(voList);

        return voPage;
    }


    @Override
    public BlogPostEntity getBlogDetail(String id) {
        return blogPostMapper.selectByIdPublished(id);
    }

}
