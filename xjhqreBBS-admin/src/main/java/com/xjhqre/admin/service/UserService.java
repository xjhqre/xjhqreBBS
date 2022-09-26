package com.xjhqre.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.admin.entity.User;
import com.xjhqre.admin.entity.dto.UserDTO;
import com.xjhqre.admin.entity.vo.UserVO;

/**
 * @Author LHR Create By 2017/8/25
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询用户信息
     * 
     * @param userDTO
     * @param current
     * @param size
     * @return
     */
    IPage<UserVO> findUser(UserDTO userDTO, Integer pageNum, Integer pageSize);

    /**
     * 查找是否存在相同邮箱
     *
     * @param uid
     * @return
     */
    Boolean hasSameEmail(String email);

    /**
     * 是否已存在相同用户名
     * 
     * @param userName
     * @return
     */
    Boolean hasSameUsername(String userName);

    /**
     * 添加用户
     * 
     * @param user
     */
    void saveUser(User user);

    /**
     * 批量删除用户
     * 
     * @param ids
     */
    void deleteUserByIds(Long[] ids);

    /**
     * 添加用户角色
     * 
     * @param uid
     * @param roleIds
     */
    void saveUserRoles(Long uid, Long[] roleIds);

    /**
     * 启用用户
     * 
     * @param ids
     */
    void enableUser(Long[] ids);

    /**
     * 批量禁用用户
     * 
     * @param ids
     */
    void disableUser(Long[] ids);
}
