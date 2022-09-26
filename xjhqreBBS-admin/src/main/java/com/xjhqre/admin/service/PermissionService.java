package com.xjhqre.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.admin.entity.Permission;
import com.xjhqre.admin.entity.dto.PermissionDTO;
import com.xjhqre.admin.entity.vo.PermissionVO;

/**
 * Created by lhr on 17-8-1.
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 根据用户id获取用户所有权限
     * 
     * @param id
     * @return
     */
    List<Permission> listUserPermission(Long uid);

    /**
     * 根据用户id，type获取用户权限
     * 
     * @param id
     * @param type
     * @return
     */
    List<Permission> loadUserPermissionByType(Long uid, Integer type);

    /**
     * 分页查询所有权限
     * 
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<PermissionVO> findPermission(PermissionDTO permissionDTO, int pageNum, int pageSize);

    /**
     * 批量删除权限
     * 
     * @param ids
     */
    void deleteByIds(Permission[] ids);

    /**
     * 查询权限列表
     * 
     * @return
     */
    List<Permission> listAll();
}
