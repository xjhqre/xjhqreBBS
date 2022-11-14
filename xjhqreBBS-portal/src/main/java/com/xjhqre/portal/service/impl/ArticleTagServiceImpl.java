package com.xjhqre.portal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.portal.ArticleTag;
import com.xjhqre.portal.mapper.ArticleTagMapper;
import com.xjhqre.portal.service.ArticleTagService;

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
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {}