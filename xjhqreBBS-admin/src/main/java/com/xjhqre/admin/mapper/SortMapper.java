package com.xjhqre.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjhqre.common.domain.portal.Sort;

/**
 * <p>
 * 文章分类 mapper层
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
@Mapper
public interface SortMapper extends BaseMapper<Sort> {

    /**
     * 统计引用次数
     *
     * @param sortId
     * @return
     */
    Integer countRef(@Param("sortId") Long sortId);

}
