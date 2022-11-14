package com.xjhqre.portal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.portal.ArticleSort;
import com.xjhqre.portal.mapper.ArticleSortMapper;
import com.xjhqre.portal.service.ArticleSortService;

/**
 * <p>
 * ArticleSortServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 10月 12, 2022
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleSortServiceImpl extends ServiceImpl<ArticleSortMapper, ArticleSort> implements ArticleSortService {}
