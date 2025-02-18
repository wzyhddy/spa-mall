package com.net.sparrow.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.sys.JobConditionEntity;
import com.net.sparrow.entity.sys.JobEntity;
import com.net.sparrow.service.sys.JobService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * 岗位 接口层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Api(tags = "岗位操作", description = "岗位接口")
@RestController
@RequestMapping("/v1/job")
public class JobController {

	@Autowired
	private JobService jobService;

	/**
	 * 通过id查询岗位信息
	 *
	 * @param id 系统ID
	 * @return 岗位信息
	 */
	@ApiOperation(notes = "通过id查询岗位信息", value = "通过id查询岗位信息")
	@GetMapping("/findById")
	public JobEntity findById(Long id) {
		return jobService.findById(id);
	}

	/**
    * 根据条件查询岗位列表
    *
    * @param jobConditionEntity 条件
    * @return 岗位列表
    */
	@ApiOperation(notes = "根据条件查询岗位列表", value = "根据条件查询岗位列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<JobEntity> searchByPage(@RequestBody JobConditionEntity jobConditionEntity) {
		return jobService.searchByPage(jobConditionEntity);
	}


	/**
     * 添加岗位
     *
     * @param jobEntity 岗位实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加岗位", value = "添加岗位")
	@PostMapping("/insert")
	public int insert(@RequestBody JobEntity jobEntity) {
		return jobService.insert(jobEntity);
	}

	/**
     * 修改岗位
     *
     * @param jobEntity 岗位实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改岗位", value = "修改岗位")
	@PostMapping("/update")
	public int update(@RequestBody JobEntity jobEntity) {
		return jobService.update(jobEntity);
	}

	/**
     * 批量删除岗位
     *
     * @param ids 岗位ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除岗位", value = "批量删除岗位")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return jobService.deleteByIds(ids);
	}
}
