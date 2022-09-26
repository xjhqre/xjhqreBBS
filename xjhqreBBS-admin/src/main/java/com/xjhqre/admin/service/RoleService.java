package com.xjhqre.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.admin.entity.Role;
import com.xjhqre.admin.entity.dto.RoleDTO;
import com.xjhqre.admin.entity.vo.RoleVO;

/**
 * Created by lhr on 17-8-1.
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户id查询用户的角色
     *
     * @param id
     * @return
     */
    List<RoleVO> listRolesByUserId(Long uid);

    /**
     * 分页查询角色列表
     * 
     * @param pageNo
     * @param length
     * @return
     */
    IPage<RoleVO> findRole(RoleDTO roleDTO, int pageNum, int pageSize);

    /**
     * 添加角色
     *
     * @param role
     * @param permissionIds
     */
    void saveRole(Role role, Long[] permissionIds);

    /**
     * 批量删除角色
     * 
     * @param ids
     */
    void deleteRole(Long[] ids);
}
