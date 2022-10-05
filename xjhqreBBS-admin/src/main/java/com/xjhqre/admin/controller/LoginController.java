// package com.xjhqre.admin.controller;
//
// import org.apache.shiro.SecurityUtils;
// import org.apache.shiro.authc.AuthenticationException;
// import org.apache.shiro.authc.LockedAccountException;
// import org.apache.shiro.authc.UsernamePasswordToken;
// import org.apache.shiro.subject.Subject;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.util.StringUtils;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.xjhqre.admin.core.BaseController;
// import com.xjhqre.admin.entity.dto.AdminDTO;
// import com.xjhqre.common.common.R;
//
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
//
// @RestController
// @Api(value = "登陆操作", tags = {"登陆操作接口"})
// public class LoginController extends BaseController {
// private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
//
// // /**
// // * 登录界面
// // *
// // * @return
// // */
// // @ApiOperation(value = "登陆界面")
// // @GetMapping(value = "/login")
// // public String login() {
// // return "login";
// // }
//
// /**
// * 用户登录
// *
// * @param user
// * @return
// */
//
// @ApiOperation(value = "用户登陆")
// @PostMapping(value = "/login")
// public R<String> login(AdminDTO user) {
// if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassWord())) {
// R.error("用户名或密码不能为空!");
// }
// Subject subject = SecurityUtils.getSubject();
// // Shiro 用来封装用户登录信息，使用用户的登录信息创建令牌 Token，登录的过程即 Shiro 验证令牌是否具有合法身份以及相关权限
// UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassWord());
// try {
// subject.login(token);
// String authorization = (String)subject.getSession().getId();
// // 将authorization传给前端，用于MySessionManager中请求的验证
// return R.success("登陆成功").add("authorization", authorization);
// } catch (LockedAccountException lae) {
// token.clear();
// return R.error("用户已经被锁定不能登录，请与管理员联系！");
// } catch (AuthenticationException e) {
// token.clear();
// return R.error("用户或密码不正确！");
// }
// }
//
// /**
// * 注销
// *
// * @return
// * @throws Exception
// */
// @GetMapping(value = "/logout")
// public R<String> logout() {
// Subject currentUser = SecurityUtils.getSubject();
// currentUser.logout();
// return R.success("注销成功");
// }
//
// }
