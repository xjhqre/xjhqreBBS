package com.xjhqre.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.domain.admin.Menu;

/**
 * 菜单表 数据层
 *
 * @author xjhqre
 */
@Mapper
public interface MenuMapper {
    /**
     * 查询系统菜单列表
     *
     * @param menu
     *            菜单信息
     * @return 菜单列表
     */
    List<Menu> selectMenuList(Menu menu);

    /**
     * 查询系统菜单列表
     * 
     * @param objectPage
     * @param menu
     * @return
     */
    IPage<Menu> findMenu(@Param("objectPage") Page<Menu> objectPage, @Param("menu") Menu menu);

    /**
     * 根据用户所有权限
     *
     * @return 权限列表
     */
    List<String> selectMenuPerms();

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu
     *            菜单信息
     * @return 菜单列表
     */
    List<Menu> selectMenuListByUserId(Menu menu);

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId
     *            角色ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId
     *            用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    List<Menu> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId
     *            用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     * 
     * @param roleId
     *            角色ID
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId
     *            菜单ID
     * @return 菜单信息
     */
    Menu selectMenuById(Long menuId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId
     *            菜单ID
     * @return 结果
     */
    int hasChildByMenuId(Long menuId);

    /**
     * 新增菜单信息
     *
     * @param menu
     *            菜单信息
     * @return 结果
     */
    int insertMenu(Menu menu);

    /**
     * 修改菜单信息
     *
     * @param menu
     *            菜单信息
     * @return 结果
     */
    int updateMenu(Menu menu);

    /**
     * 删除菜单管理信息
     *
     * @param menuId
     *            菜单ID
     * @return 结果
     */
    int deleteMenuById(Long menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName
     *            菜单名称
     * @param parentId
     *            父菜单ID
     * @return 结果
     */
    Menu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);

}
