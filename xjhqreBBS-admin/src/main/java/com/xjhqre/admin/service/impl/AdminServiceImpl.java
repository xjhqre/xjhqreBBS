package com.xjhqre.admin.service.impl;

import java.util.Arrays;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.entity.Admin;
import com.xjhqre.admin.entity.AdminRole;
import com.xjhqre.admin.entity.dto.AdminDTO;
import com.xjhqre.admin.entity.vo.AdminVO;
import com.xjhqre.admin.mapper.AdminMapper;
import com.xjhqre.admin.mapper.AdminRoleMapper;
import com.xjhqre.admin.mapper.PermissionMapper;
import com.xjhqre.admin.mapper.RolePermissionMapper;
import com.xjhqre.admin.service.AdminService;
import com.xjhqre.admin.service.RoleService;
import com.xjhqre.common.exception.ErrorEnume;
import com.xjhqre.common.exception.MyServiceException;

/**
 * Created by lhr on 17-8-1.
 */
@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 根据名字查询管理员
     * 
     * @param username
     * @return
     */
    @Override
    public AdminVO getAdminByUsername(String username) {
        return this.adminMapper.getAdminUserByUserName(username);
    }

    /**
     * 分页查询管理员列表
     * 
     * @param adminDTO
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<AdminVO> findAdmin(AdminDTO adminDTO, Integer pageNum, Integer pageSize) {
        return this.adminMapper.findAdmin(new Page<>(pageNum, pageSize), adminDTO);
    }

    /**
     * 添加管理员
     * 
     * @param adminDTO
     */
    @Override
    public void saveAdmin(AdminDTO adminDTO) {
        // 查找是否有重名管理员
        AdminVO byUserName = this.getAdminByUsername(adminDTO.getUserName());
        if (byUserName != null) {
            throw new MyServiceException(ErrorEnume.USERNAME_DUPLICATE);
        }
        adminDTO.setStatus(1);
        // 盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        // 加密
        SimpleHash simpleHash = new SimpleHash("md5", adminDTO.getPassWord(), salt, 1);
        String NewPassword = simpleHash.toString();
        adminDTO.setPassWord(NewPassword);
        adminDTO.setSalt(salt);
        this.adminMapper.insert(adminDTO);
    }

    /**
     * 添加管理员角色
     * 
     * @param uid
     * @param roleIds
     */
    @Override
    public void saveAdminRoles(Long uid, Long[] roleIds) {
        // TODO 校验，抛出异常
        for (Long roleId : roleIds) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminUid(uid);
            adminRole.setRoleUid(roleId);
            this.adminRoleMapper.insert(adminRole);
        }
    }

    /**
     * 批量删除管理员
     *
     * @param ids
     */
    @Override
    public void deleteAdminByIds(Long[] ids) {
        this.adminMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 启用管理员
     * 
     * @param ids
     */
    @Override
    public void enableAdmin(Long[] ids) {
        this.adminMapper.enableAdminUser(ids);
    }

    @Override
    public void disableAdmin(Long[] ids) {
        this.adminMapper.disableAdminUser(ids);
    }

}
