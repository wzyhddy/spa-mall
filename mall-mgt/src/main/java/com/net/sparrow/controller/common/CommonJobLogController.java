package com.net.sparrow.controller.common;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.common.CommonJobLogConditionEntity;
import com.net.sparrow.entity.common.CommonJobLogEntity;
import com.net.sparrow.service.common.CommonJobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 定时任务执行日志 接口层
 */
@Api(tags = "定时任务执行日志操作", description = "定时任务执行日志接口")
@RestController
@RequestMapping("/v1/commonJobLog")
public class CommonJobLogController {

	@Autowired
	private CommonJobLogService commonJobLogService;

	/**
	 * 通过id查询定时任务执行日志信息
	 *
	 * @param id 系统ID
	 * @return 定时任务执行日志信息
	 */
	@ApiOperation(notes = "通过id查询定时任务执行日志信息", value = "通过id查询定时任务执行日志信息")
	@GetMapping("/findById")
	public CommonJobLogEntity findById(Long id) {
		return commonJobLogService.findById(id);
	}

	/**
    * 根据条件查询定时任务执行日志列表
    *
    * @param commonJobLogConditionEntity 条件
    * @return 定时任务执行日志列表
    */
	@ApiOperation(notes = "根据条件查询定时任务执行日志列表", value = "根据条件查询定时任务执行日志列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<CommonJobLogEntity> searchByPage(@RequestBody CommonJobLogConditionEntity commonJobLogConditionEntity) {
		return commonJobLogService.searchByPage(commonJobLogConditionEntity);
	}


	/**
     * 添加定时任务执行日志
     *
     * @param commonJobLogEntity 定时任务执行日志实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加定时任务执行日志", value = "添加定时任务执行日志")
	@PostMapping("/insert")
	public int insert(@RequestBody CommonJobLogEntity commonJobLogEntity) {
		return commonJobLogService.insert(commonJobLogEntity);
	}

	/**
     * 修改定时任务执行日志
     *
     * @param commonJobLogEntity 定时任务执行日志实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改定时任务执行日志", value = "修改定时任务执行日志")
	@PostMapping("/update")
	public int update(@RequestBody CommonJobLogEntity commonJobLogEntity) {
		return commonJobLogService.update(commonJobLogEntity);
	}

	/**
     * 批量删除定时任务执行日志
     *
     * @param ids 定时任务执行日志ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除定时任务执行日志", value = "批量删除定时任务执行日志")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return commonJobLogService.deleteByIds(ids);
	}
}
