// package com.xjhqre.admin.controller;
//
// import java.util.Arrays;
// import java.util.List;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.quark.common.dto.PageResult;
// import com.quark.common.dto.QuarkResult;
// import com.quark.common.entity.Posts;
// import com.quark.common.entity.User;
// import com.xjhqre.admin.entity.dto.AdminUserDTO;
// import com.xjhqre.admin.entity.vo.AdminUserVO;
// import com.xjhqre.admin.service.PostsService;
// import com.xjhqre.admin.service.UserService;
// import com.xjhqre.common.common.R;
//
// import io.swagger.annotations.ApiImplicitParam;
// import io.swagger.annotations.ApiImplicitParams;
// import io.swagger.annotations.ApiOperation;
//
/// **
// * @Author LHR Create By 2017/9/3
// */
// @RequestMapping("/posts")
// @RestController
// public class PostsController {
//
// @Autowired
// private UserService userService;
//
// @Autowired
// private PostsService postsService;
//
// @ApiOperation("分页查询帖子")
// @ApiImplicitParams({
// @ApiImplicitParam(name = "size", value = "正整数，表示每页几条记录", required = true, dataType = "int", example = "20"),
// @ApiImplicitParam(name = "current", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1")})
// @GetMapping("findPosts/{size}/{current}")
// public R<IPage<AdminUserVO>> findPosts(AdminUserDTO adminDTO, @PathVariable("size") Integer size,
// @PathVariable("current") Integer current) {
// return R.success(this.adminUserService.findAll(adminDTO, current, size));
// }
//
// @GetMapping
// public PageResult getAll(Posts posts, Integer uid, String draw,
// @RequestParam(required = false, defaultValue = "1") int start,
// @RequestParam(required = false, defaultValue = "10") int length) {
// int pageNo = start / length;
// if (uid != null) {
// User user = this.userService.findOne(uid);
// posts.setUser(user);
// }
// Page<Posts> page = this.postsService.findByPage(posts, pageNo, length);
// PageResult<List<Posts>> result =
// new PageResult<>(draw, page.getTotalElements(), page.getTotalElements(), page.getContent());
// return result;
// }
//
// @PostMapping("/saveTop")
// public QuarkResult saveTop(@RequestParam(value = "id[]") Integer[] id) {
// QuarkResult result = restProcessor(() -> {
// this.postsService.changeTop(id);
// return QuarkResult.ok();
// });
// return result;
// }
//
// @PostMapping("/saveGood")
// public QuarkResult saveGood(@RequestParam(value = "id[]") Integer[] id) {
// QuarkResult result = restProcessor(() -> {
// this.postsService.changeGood(id);
// return QuarkResult.ok();
// });
// return result;
// }
//
// @PostMapping("/delete")
// public QuarkResult deletePosts(@RequestParam(value = "id[]") Posts[] id) {
// QuarkResult result = restProcessor(() -> {
// this.postsService.deleteInBatch(Arrays.asList(id));
// return QuarkResult.ok();
// });
// return result;
// }
// }
