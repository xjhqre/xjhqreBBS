package com.xjhqre.portal.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.portal.Collect;
import com.xjhqre.common.domain.portal.vo.CollectVO;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.portal.mapper.CollectMapper;
import com.xjhqre.portal.service.ArticleService;
import com.xjhqre.portal.service.CollectService;

/**
 * <p>
 * 文章收藏服务类
 * </p>
 *
 * @author xjhqre
 * @since 10月 20, 2022
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Autowired
    CollectMapper collectMapper;

    @Autowired
    ArticleService articleService;

    /**
     * 查询用户个人收藏
     * 
     * @param collect
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<CollectVO> findCollect(Collect collect, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, collect.getUserId())
            // 如果有标题则按标题查询
            .eq(collect.getArticleTitle() != null, Collect::getArticleTitle, collect.getArticleTitle())
            // 按收藏时间倒序排序
            .orderByDesc(Collect::getCreateTime);
        Page<Collect> collectPage = this.collectMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        Page<CollectVO> collectVOPage = new Page<>(pageNum, pageSize);
        BeanUtils.copyProperties(collectPage, collectVOPage);
        List<CollectVO> collectVOS = collectPage.getRecords().stream().map(record -> {
            CollectVO collectVO = new CollectVO();
            Long articleId = record.getArticleId();
            Article article = this.articleService.selectArticleById(articleId);
            BeanUtils.copyProperties(record, collectVO);
            collectVO.setSummary(article.getSummary());
            collectVO.setAuthor(article.getAuthor());
            collectVO.setCollectCount(article.getCollectCount());
            collectVO.setViewCount(article.getViewCount());
            collectVO.setThumbCount(article.getThumbCount());
            collectVO.setCover(article.getCover());
            return collectVO;
        }).collect(Collectors.toList());
        collectVOPage.setRecords(collectVOS);
        return collectVOPage;
    }

    /**
     * 添加收藏
     * 
     * @param collect
     */
    @Override
    public void addCollect(Collect collect) {
        if (collect.getUserId() == null) {
            collect.setUserId(SecurityUtils.getUserId());
        }
        this.collectMapper.insert(collect);
    }

    /**
     * 取消收藏
     * 
     * @param collectId
     */
    @Override
    public void deleteCollect(Long collectId) {
        this.collectMapper.deleteById(collectId);
    }
}
