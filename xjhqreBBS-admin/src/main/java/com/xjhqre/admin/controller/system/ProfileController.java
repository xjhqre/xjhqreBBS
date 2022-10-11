package com.xjhqre.admin.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xjhqre.admin.core.BaseController;
import com.xjhqre.admin.security.service.TokenService;
import com.xjhqre.admin.service.UserService;
import com.xjhqre.admin.utils.OSSUtil;
import com.xjhqre.admin.utils.OSSUtil.FileDirType;
import com.xjhqre.common.annotation.Log;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.model.LoginUser;
import com.xjhqre.common.enums.BusinessType;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.StringUtils;

import io.swagger.annotations.Api;

/**
 * 个人信息 业务处理
 * 
 * @author xjhqre
 */
@RestController
@Api(value = "个人信息接口", tags = "个人信息接口")
@RequestMapping("/system/user/profile")
public class ProfileController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 个人信息附加角色组
     */
    @GetMapping
    public R<User> profile() {
        LoginUser loginUser = this.getLoginUser();
        User user = loginUser.getUser();
        String roleGroup = this.userService.selectUserRoleGroup(loginUser.getUsername());
        return R.success(user).add("roleGroup", roleGroup);
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> updateProfile(@RequestBody User user) {
        LoginUser loginUser = this.getLoginUser();
        User sysUser = loginUser.getUser();
        user.setUserName(sysUser.getUserName());
        if (StringUtils.isNotEmpty(user.getMobile())
            && Constants.NOT_UNIQUE.equals(this.userService.checkPhoneUnique(user))) {
            return R.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail())
            && Constants.NOT_UNIQUE.equals(this.userService.checkEmailUnique(user))) {
            return R.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        user.setAvatar(null);
        if (this.userService.updateUserProfile(user) > 0) {
            // 更新缓存用户信息
            sysUser.setNickName(user.getNickName());
            sysUser.setMobile(user.getMobile());
            sysUser.setEmail(user.getEmail());
            sysUser.setSex(user.getSex());
            this.tokenService.setLoginUser(loginUser);
            return R.success("修改个人信息成功");
        }
        return R.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public R<String> updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = this.getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return R.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return R.error("新密码不能与旧密码相同");
        }
        if (this.userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            this.tokenService.setLoginUser(loginUser);
            return R.success("重置密码成功");
        }
        return R.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public R<String> avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = this.getLoginUser();
            String avatarUrl = OSSUtil.upload(file, FileDirType.AVATAR);
            if (this.userService.updateUserAvatar(loginUser.getUsername(), avatarUrl)) {
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatarUrl);
                this.tokenService.setLoginUser(loginUser);
                return R.success("修改头像成功").add("imgUrl", avatarUrl);
            }
        }
        return R.error("上传图片异常，请联系管理员");
    }
}
