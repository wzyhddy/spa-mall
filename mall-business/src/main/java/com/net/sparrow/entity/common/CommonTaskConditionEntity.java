

package com.net.sparrow.entity.common;

import com.net.sparrow.entity.RequestPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 任务查询条件实体
 */
@ApiModel("任务查询条件实体")
@Data
public class CommonTaskConditionEntity extends RequestPageEntity {


	/**
	 * 执行状态集合
	 */
	private List<Integer> statusList;

	/**
	 * ID
	 */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 * 任务名称
	 */
	@ApiModelProperty("任务名称")
	private String name;

	/**
	 * 下载文件地址
	 */
	@ApiModelProperty("下载文件地址")
	private String fileUrl;

	/**
	 * 任务类型 1：通用excel导出
	 */
	@ApiModelProperty("任务类型 1：通用excel导出")
	private Integer type;

	/**
	 * 执行状态 0：待执行 1：执行中 2：成功 3：失败
	 */
	@ApiModelProperty("执行状态 0：待执行 1：执行中 2：成功 3：失败")
	private Integer status;

	/**
	 * 失败次数
	 */
	@ApiModelProperty("失败次数")
	private Integer failureCount;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty("创建人ID")
	private Long createUserId;

	/**
	 * 创建人名称
	 */
	@ApiModelProperty("创建人名称")
	private String createUserName;

	/**
	 * 创建日期
	 */
	@ApiModelProperty("创建日期")
	private Date createTime;

	/**
	 * 修改人ID
	 */
	@ApiModelProperty("修改人ID")
	private Long updateUserId;

	/**
	 * 修改人名称
	 */
	@ApiModelProperty("修改人名称")
	private String updateUserName;

	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	private Date updateTime;

	/**
	 * 是否删除 1：已删除 0：未删除
	 */
	@ApiModelProperty("是否删除 1：已删除 0：未删除")
	private Integer isDel;

	/**
	 * 业务类型 1：菜单 2：部门 3：角色 4：用户
	 */
	@ApiModelProperty("业务类型 1：菜单 2：部门 3：角色 4：用户")
	private Integer bizType;

	/**
	 * 请求参数
	 */
	@ApiModelProperty("请求参数")
	private String requestParam;
}
