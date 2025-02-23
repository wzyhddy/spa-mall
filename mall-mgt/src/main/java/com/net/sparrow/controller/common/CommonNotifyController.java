package com.net.sparrow.controller.common;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.common.CommonNotifyConditionEntity;
import com.net.sparrow.entity.common.CommonNotifyEntity;
import com.net.sparrow.service.common.CommonNotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 通知 接口层
 */
@Api(tags = "通知操作", description = "通知接口")
@RestController
@RequestMapping("/v1/commonNotify")
public class CommonNotifyController {

	@Autowired
	private CommonNotifyService commonNotifyService;

	/**
	 * 通过id查询通知信息
	 *
	 * @param id 系统ID
	 * @return 通知信息
	 */
	@ApiOperation(notes = "通过id查询通知信息", value = "通过id查询通知信息")
	@GetMapping("/findById")
	public CommonNotifyEntity findById(Long id) {
		return commonNotifyService.findById(id);
	}

	/**
    * 根据条件查询通知列表
    *
    * @param commonNotifyConditionEntity 条件
    * @return 通知列表
    */
	@ApiOperation(notes = "根据条件查询通知列表", value = "根据条件查询通知列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<CommonNotifyEntity> searchByPage(@RequestBody CommonNotifyConditionEntity commonNotifyConditionEntity) {
		return commonNotifyService.searchByPage(commonNotifyConditionEntity);
	}


	/**
     * 添加通知
     *
     * @param commonNotifyEntity 通知实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加通知", value = "添加通知")
	@PostMapping("/insert")
	public int insert(@RequestBody CommonNotifyEntity commonNotifyEntity) {
		return commonNotifyService.insert(commonNotifyEntity);
	}

	/**
     * 修改通知
     *
     * @param commonNotifyEntity 通知实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改通知", value = "修改通知")
	@PostMapping("/update")
	public int update(@RequestBody CommonNotifyEntity commonNotifyEntity) {
		return commonNotifyService.update(commonNotifyEntity);
	}

	/**
     * 删除通知
     *
     * @param id 通知ID
     * @return 影响行数
     */
	@ApiOperation(notes = "删除通知", value = "删除通知")
	@PostMapping("/deleteById")
	public int deleteById(@RequestBody @NotNull Long id) {
		return commonNotifyService.deleteById(id);
	}
}
