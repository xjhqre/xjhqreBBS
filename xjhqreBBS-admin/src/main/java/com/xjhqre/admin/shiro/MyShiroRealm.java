package com.xjhqre.admin.shiro;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.xjhqre.admin.entity.Permission;
import com.xjhqre.admin.entity.vo.AdminVO;
import com.xjhqre.admin.entity.vo.RoleVO;
import com.xjhqre.admin.service.AdminService;
import com.xjhqre.admin.service.PermissionService;
import com.xjhqre.admin.service.RoleService;

/**
 * Created by lhr on 17-8-1.
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private AdminService adminService;

    @Resource
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    /**
     * 授权
     * 
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        AdminVO currentAdmin = (AdminVO)principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        List<Permission> permissionList = this.permissionService.listUserPermission(currentAdmin.getUid());
        // 向 SimpleAuthorizationInfo 中添加授权信息
        permissionList.forEach(p -> info.addStringPermission(p.getPerUrl()));

        // 查询出当前用户所有的角色放入 info 中
        List<RoleVO> roleVOS = this.roleService.listRolesByUserId(currentAdmin.getUid());
        roleVOS.forEach(roleVO -> info.addRole(String.valueOf(roleVO.getUid())));
        return info;
    }

    /**
     * 认证，即验证用户输入的账号和密码是否正确
     * 
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户的输入的账号.
        String username = (String)token.getPrincipal();

        // 查询数据库中的管理员信息
        AdminVO user = this.adminService.getAdminByUsername(username);
        if (user == null)
            throw new UnknownAccountException();
        if (0 == user.getStatus()) {
            throw new LockedAccountException(); // 帐号锁定
        }

        // 认证信息token里存放账号密码, getName() 是当前Realm的继承方法,通常返回当前类名
        // 盐也放进去
        // 这样通过配置中的 HashedCredentialsMatcher 进行自动校验
        // 这块对比逻辑是先对比username，但是username肯定是相等的，所以真正对比的是password
        // 从这里传入的password（这里是从数据库获取的）和token（filter中登录时生成的）中的password做对比，如果相同就允许登录，不相同就抛出异常
        String Salt = user.getSalt();
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, // 用户
            user.getPassWord(), // 密码，这里是指从数据库中获取的password
            ByteSource.Util.bytes(Salt), // 将用户名作为 salt
            user.getUserName() // realm name
        );

        // 把用户信息放在session里
        // Session session = SecurityUtils.getSubject().getSession();
        // session.setAttribute("AdminSession", user);
        // session.setAttribute("AdminSessionId", user.getUid());
        return authenticationInfo;
    }
}
