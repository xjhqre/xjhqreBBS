// package com.xjhqre.admin.controller;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.xjhqre.admin.core.BaseController;
// import com.xjhqre.common.common.R;
// import com.xjhqre.common.constant.ErrorCode;
// import com.xjhqre.common.domain.entity.User;
// import com.xjhqre.common.exception.ServiceException;
// import com.xjhqre.admin.service.UserService;
//
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiImplicitParam;
// import io.swagger.annotations.ApiImplicitParams;
// import io.swagger.annotations.ApiOperation;
//
// @RestController
// @RequestMapping("/user")
// @Api(value = "用户操作接口", tags = {"用户操作接口"})
// public class UserController extends BaseController {
//
// @Autowired
// private UserService userService;
//
// @ApiOperation("分页查询用户列表")
// @ApiImplicitParams({
// @ApiImplicitParam(name = "size", value = "正整数，表示每页几条记录", required = true, dataType = "int", example = "20"),
// @ApiImplicitParam(name = "current", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1")})
// @GetMapping("findUser/{size}/{current}")
// public R<IPage<User>> findUser(User user, @PathVariable("size") Integer size,
// @PathVariable("current") Integer current) {
// return R.success(this.userService.findUser(user, current, size));
// }
//
// @ApiOperation(value = "添加用户")
// @PostMapping("/addUser")
// public R<String> addUser(User user) {
// // 查找是否有重名邮箱
// Boolean hasSameEmail = this.userService.hasSameEmail(user.getEmail());
// if (hasSameEmail) {
// throw new ServiceException(ErrorCode.EMAIL_DUPLICATE, "该邮箱已存在");
// }
// // 查找是否有重名用户
// Boolean hasSameUsername = this.userService.hasSameUsername(user.getUserName());
// if (hasSameUsername) {
// throw new ServiceException(ErrorCode.USERNAME_DUPLICATE, "该用户名已存在");
// }
//
// this.userService.saveUser(user);
//
// return R.success("添加用户成功");
// }
//
// @ApiOperation(value = "删除用户")
// @PostMapping("/deleteUser")
// public R<String> deleteUser(@RequestParam(value = "ids") Long[] ids) {
// this.userService.deleteUserByIds(ids);
// return R.success("删除管理员成功");
// }
//
// /**
// * 添加用户角色
// *
// * @param uid
// * @param roleIds
// * @return
// */
// @ApiOperation(value = "给用户添加角色")
// @PostMapping("/saveUserRoles")
// public R<String> saveUserRoles(Long uid, Long[] roleIds) {
// this.userService.saveUserRoles(uid, roleIds);
// return R.success("添加角色成功");
// }
//
// // TODO 删除用户角色
//
// /**
// * 启用用户
// *
// * @param ids
// * @return
// */
// @ApiOperation(value = "启用用户")
// @PostMapping("/enableUser")
// public R<String> enableUser(@RequestParam(value = "ids") Long[] ids) {
// this.userService.enableUser(ids);
// return R.success("启用用户成功");
// }
//
// /**
// * 禁用用户
// *
// * @param ids
// * @return
// */
// @ApiOperation(value = "禁用用户")
// @PostMapping("/disableUser")
// public R<String> disableUser(@RequestParam(value = "ids") Long[] ids) {
// this.userService.disableUser(ids);
// return R.success("禁用用户成功");
// }
//
// }
