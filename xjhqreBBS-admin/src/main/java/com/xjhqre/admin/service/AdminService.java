package com.xjhqre.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.admin.entity.Admin;
import com.xjhqre.admin.entity.dto.AdminDTO;
import com.xjhqre.admin.entity.vo.AdminVO;

/**
 * Created by lhr on 17-8-1.
 */

public interface AdminService extends IService<Admin> {

    /**
     * 根据用户名查找用户
     * 
     * @param username
     * @return
     */
    AdminVO getAdminByUsername(String username);

    /**
     * 分页查询管理员
     * 
     * @param adminDTO
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<AdminVO> findAdmin(AdminDTO adminDTO, Integer pageNum, Integer pageSize);

    /**
     * 保存用户
     * 
     * @param adminDTO
     * @return
     */
    void saveAdmin(AdminDTO adminDTO);

    /**
     * 保存用户的角色
     * 
     * @param uid
     * @param roles
     * @return
     */
    void saveAdminRoles(Long uid, Long[] roleIds);

    void deleteAdminByIds(Long[] ids);

    /**
     * 开启管理员
     *
     * @param ids
     */
    void enableAdmin(Long[] ids);

    /**
     * 禁用管理员
     * 
     * @param ids
     */
    void disableAdmin(Long[] ids);

}
