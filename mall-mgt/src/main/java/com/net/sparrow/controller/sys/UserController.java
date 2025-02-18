package com.net.sparrow.controller.sys;

import com.net.sparrow.annotation.NoLogin;
import com.net.sparrow.util.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.sys.UserConditionEntity;
import com.net.sparrow.entity.sys.UserEntity;
import com.net.sparrow.service.sys.UserService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * 用户 接口层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Api(tags = "用户操作", description = "用户接口")
@RestController
@RequestMapping("/v1/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 通过id查询用户信息
	 *
	 * @param id 系统ID
	 * @return 用户信息
	 */
	@ApiOperation(notes = "通过id查询用户信息", value = "通过id查询用户信息")
	@GetMapping("/findById")
	@PreAuthorize("hasAnyRole('USER')")
	public UserEntity findById(Long id) {
		return userService.findById(id);
	}

	/**
	 * 根据条件查询用户列表
	 *
	 * @param userConditionEntity 条件
	 * @return 用户列表
	 */
	@ApiOperation(notes = "根据条件查询用户列表", value = "根据条件查询用户列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<UserEntity> searchByPage(@RequestBody UserConditionEntity userConditionEntity) {
		return userService.searchByPage(userConditionEntity);
	}


	/**
     * 添加用户
     *
     * @param userEntity 用户实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加用户", value = "添加用户")
	@PostMapping("/insert")
	@NoLogin
	public void insert(@RequestBody UserEntity userEntity) {
		userService.insert(userEntity);
	}

	/**
     * 修改用户
     *
     * @param userEntity 用户实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改用户", value = "修改用户")
	@PostMapping("/update")
	public int update(@RequestBody UserEntity userEntity) {
		return userService.update(userEntity);
	}

	/**
     * 批量删除用户
     *
     * @param ids 用户ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除用户", value = "批量删除用户")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return userService.deleteByIds(ids);
	}
}
