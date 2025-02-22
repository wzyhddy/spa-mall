package com.net.sparrow.controller.common;

import com.net.sparrow.annotation.NoLogin;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.common.CommonJobConditionEntity;
import com.net.sparrow.entity.common.CommonJobEntity;
import com.net.sparrow.service.common.CommonJobService;
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
 * 定时任务 接口层
 */
@Api(tags = "定时任务操作", description = "定时任务接口")
@RestController
@RequestMapping("/v1/commonJob")
public class CommonJobController {

    @Autowired
    private CommonJobService commonJobService;

    /**
     * 通过id查询定时任务信息
     *
     * @param id 系统ID
     * @return 定时任务信息
     */
    @ApiOperation(notes = "通过id查询定时任务信息", value = "通过id查询定时任务信息")
    @GetMapping("/findById")
    public CommonJobEntity findById(Long id) {
        return commonJobService.findById(id);
    }

    /**
     * 根据条件查询定时任务列表
     *
     * @param commonJobConditionEntity 条件
     * @return 定时任务列表
     */
    @ApiOperation(notes = "根据条件查询定时任务列表", value = "根据条件查询定时任务列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<CommonJobEntity> searchByPage(@RequestBody CommonJobConditionEntity commonJobConditionEntity) {
        return commonJobService.searchByPage(commonJobConditionEntity);
    }


    /**
     * 添加定时任务
     *
     * @param commonJobEntity 定时任务实体
     * @return 影响行数
     */
    @NoLogin
    @ApiOperation(notes = "添加定时任务", value = "添加定时任务")
    @PostMapping("/insert")
    public int insert(@RequestBody CommonJobEntity commonJobEntity) {
        return commonJobService.insert(commonJobEntity);
    }

    /**
     * 修改定时任务
     *
     * @param commonJobEntity 定时任务实体
     * @return 影响行数
     */
    @ApiOperation(notes = "修改定时任务", value = "修改定时任务")
    @PostMapping("/update")
    public int update(@RequestBody CommonJobEntity commonJobEntity) {
        return commonJobService.update(commonJobEntity);
    }

    /**
     * 批量删除定时任务
     *
     * @param ids 定时任务ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除定时任务", value = "批量删除定时任务")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return commonJobService.deleteByIds(ids);
    }
}
