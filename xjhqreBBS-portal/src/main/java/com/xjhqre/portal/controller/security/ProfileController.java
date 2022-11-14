package com.xjhqre.portal.controller.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.annotation.Log;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.core.BaseController;
import com.xjhqre.common.domain.LoginUser;
import com.xjhqre.common.domain.admin.Role;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.sms.Message3;
import com.xjhqre.common.enums.BusinessType;
import com.xjhqre.common.utils.FileUtils;
import com.xjhqre.common.utils.OSSUtil;
import com.xjhqre.common.utils.OSSUtil.FileDirType;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.common.utils.uuid.IdUtils;
import com.xjhqre.portal.security.service.TokenService;
import com.xjhqre.portal.service.RoleService;
import com.xjhqre.portal.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 个人信息 业务处理
 * 
 * @author xjhqre
 */
@RestController
@Api(value = "个人信息接口", tags = "个人信息接口")
@RequestMapping("/portal/profile")
public class ProfileController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    RoleService roleService;

    /**
     * 个人信息附加角色组
     */
    @ApiOperation(value = "获取个人信息以及角色组")
    @GetMapping
    public R<User> profile() {
        LoginUser loginUser = this.getLoginUser();
        User user = loginUser.getUser();
        List<Role> roles = this.roleService.selectRolesByUserId(loginUser.getUserId());
        return R.success(user).add("roleGroup", roles);
    }

    /**
     * 修改用户
     */
    @ApiOperation(value = "修改用户信息")
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> updateProfile(@RequestBody User user) {
        // 校验
        if (StringUtils.isNotEmpty(user.getUserName())
            && Constants.NOT_UNIQUE.equals(this.userService.checkUserNameUnique(user))) {
            return R.error("修改用户'" + user.getUserName() + "'失败，用户名称已存在");
        }
        if (StringUtils.isNotEmpty(user.getMobile())
            && Constants.NOT_UNIQUE.equals(this.userService.checkPhoneUnique(user))) {
            return R.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail())
            && Constants.NOT_UNIQUE.equals(this.userService.checkEmailUnique(user))) {
            return R.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        this.userService.updateById(user);

        // 更新缓存用户信息
        LoginUser loginUser = this.getLoginUser();
        User sysUser = loginUser.getUser();
        sysUser.setNickName(user.getNickName());
        sysUser.setMobile(user.getMobile());
        sysUser.setEmail(user.getEmail());
        sysUser.setBirthday(user.getBirthday());
        sysUser.setSummary(user.getSummary());
        sysUser.setSex(user.getSex());
        loginUser.setUser(sysUser);
        this.tokenService.setLoginUser(loginUser);
        return R.success("修改个人信息成功");
    }

    /**
     * 重置密码
     */
    @ApiOperation(value = "重置密码")
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public R<String> updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = this.getLoginUser();
        Long userId = loginUser.getUserId();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return R.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return R.error("新密码不能与旧密码相同");
        }
        this.userService.resetUserPwd(userId, SecurityUtils.encryptPassword(newPassword));
        // 更新缓存用户密码
        loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
        this.tokenService.setLoginUser(loginUser);
        return R.success("重置密码成功");
    }

    /**
     * 头像上传
     */
    @ApiOperation(value = "上传头像")
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public R<String> avatar(@RequestParam("avatarfile") MultipartFile file) {
        if (!file.isEmpty()) {
            LoginUser loginUser = this.getLoginUser();
            String avatarUrl = OSSUtil.upload(file, FileDirType.AVATAR,
                IdUtils.simpleUUID() + FileUtils.getExtension(file.getOriginalFilename()));
            this.userService.updateUserAvatar(loginUser.getUserId(), avatarUrl);
            // 更新缓存用户头像
            loginUser.getUser().setAvatar(avatarUrl);
            this.tokenService.setLoginUser(loginUser);
            return R.success("修改头像成功").add("imgUrl", avatarUrl);
        }
        return R.error("上传图片异常，请联系管理员");
    }

    @ApiOperation(value = "查询用户消息-点赞或收藏")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("findMessage3/{pageNum}/{pageSize}")
    public R<IPage<Message3>> findMessage3(Long userId, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.userService.findMessage3(userId, pageNum, pageSize));
    }
}
