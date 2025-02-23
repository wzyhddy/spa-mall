package com.net.sparrow.entity.common;

import com.net.sparrow.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 敏感词实体
 */
@ApiModel("敏感词实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonSensitiveWordEntity extends BaseEntity {


	/**
	 * 类型 1:广告 2:政治 3：违法 4：色情 5：网址
	 */
	@ApiModelProperty("类型 1:广告 2:政治 3：违法 4：色情 5：网址")
	private Integer type;

	/**
	 * 名称
	 */
	@ApiModelProperty("名称")
	private String word;
}
