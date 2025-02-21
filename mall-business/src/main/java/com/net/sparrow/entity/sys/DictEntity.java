package com.net.sparrow.entity.sys;

import com.net.sparrow.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据字典实体
 */
@ApiModel("数据字典实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DictEntity extends BaseEntity {


	/**
	 * 父字段ID
	 */
	@ApiModelProperty("父字段ID")
	private Long parentId;

	/**
	 * 字典名称
	 */
	@ApiModelProperty("字典名称")
	private String dictName;

	/**
	 * 字典描述
	 */
	@ApiModelProperty("字典描述")
	private String dictDescription;

	/**
	 * 字典详情
	 */
	@ApiModelProperty("字典详情")
	private List<DictDetailEntity> detailList;
}
