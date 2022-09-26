package com.xjhqre.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.entity.Admin;
import com.xjhqre.admin.entity.dto.AdminDTO;
import com.xjhqre.admin.entity.vo.AdminVO;

/**
 * <p>
 * AdminMapper
 * </p>
 *
 * @author xjhqre
 * @since 9月 17, 2022
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 查询管理员
     * 
     * @param page
     * @param queryParam
     * @return
     */
    IPage<AdminVO> findAdmin(@Param("page") Page<AdminVO> page, @Param("queryParam") AdminDTO queryParam);

    /**
     * 根据姓名查询管理员
     * 
     * @param username
     * @return
     */
    AdminVO getAdminUserByUserName(@Param("username") String username);

    /**
     * 批量启用管理员
     * 
     * @param ids
     * @return
     */
    void enableAdminUser(@Param("ids") Long[] ids);

    void disableAdminUser(@Param("ids") Long[] ids);
}
