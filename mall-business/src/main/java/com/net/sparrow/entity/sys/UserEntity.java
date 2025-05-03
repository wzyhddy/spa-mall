package com.net.sparrow.entity.sys;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户实体
 * 
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@ApiModel("用户实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity extends BaseEntity {
	

	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private Long avatarId;

	/**
	 * 邮箱
	 */
	@ApiModelProperty("邮箱")
	private String email;

	/**
	 * 密码
	 */
	@ApiModelProperty("密码")
	private String password;

	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private String userName;

	/**
	 * 部门ID
	 */
	@ApiModelProperty("部门ID")
	private Long deptId;

	/**
	 * 部门名称
	 */
	@ApiModelProperty("部门名称")
	private String deptName;

	/**
	 * 手机号码
	 */
	@ApiModelProperty("手机号码")
	private String phone;

	/**
	 * 岗位ID
	 */
	@ApiModelProperty("岗位ID")
	private Long jobId;

	/**
	 * 最后修改密码的时间
	 */
	@ApiModelProperty("最后修改密码的时间")
	private Date lastChangePasswordTime;

	/**
	 * 别名
	 */
	@ApiModelProperty("别名")
	private String nickName;

	/**
	 * 性别 1：男 2：女
	 */
	@ApiModelProperty("性别 1：男 2：女")
	private Integer sex;

	/**
	 * 有效状态 1:有效 0:无效
	 */
	@ApiModelProperty("有效状态 1:有效 0:无效")
	private Integer validStatus;

	/**
	 * 角色列表
	 */
	@ApiModelProperty("角色列表")
	private List<RoleEntity> roleList;

	/**
	 * 最后登录城市
	 */
	@ApiModelProperty("最后登录城市")
	private String lastLoginCity;

	/**
	 * 最后登录时间
	 */
	@ApiModelProperty("最后登录时间")
	private Date lastLoginTime;

	@ApiModelProperty("部门")
	private DeptEntity dept;

	/**
	 * 岗位
	 */
	@ApiModelProperty("岗位")
	private List<JobEntity> jobs;

	/**
	 * 角色列表
	 */
	@ApiModelProperty("角色列表")
	private List<RoleEntity> roles;
}
