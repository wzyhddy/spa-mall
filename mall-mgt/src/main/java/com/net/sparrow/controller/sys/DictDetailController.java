package com.net.sparrow.controller.sys;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.sys.DictDetailConditionEntity;
import com.net.sparrow.entity.sys.DictDetailEntity;
import com.net.sparrow.service.sys.DictDetailService;
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
 * 部门 接口层
 */
@Api(tags = "部门操作", description = "部门接口")
@RestController
@RequestMapping("/v1/dictDetail")
public class DictDetailController {

    @Autowired
    private DictDetailService dictDetailService;

    /**
     * 通过id查询数据字典详情信息
     *
     * @param id 系统ID
     * @return 数据字典详情信息
     */
    @ApiOperation(notes = "通过id查询数据字典详情信息", value = "通过id查询数据字典详情信息")
    @GetMapping("/findById")
    public DictDetailEntity findById(Long id) {
        return dictDetailService.findById(id);
    }

    /**
     * 根据条件查询数据字典详情列表
     *
     * @param dictDetailConditionEntity 条件
     * @return 数据字典详情列表
     */
    @ApiOperation(notes = "根据条件查询数据字典详情列表", value = "根据条件查询数据字典详情列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<DictDetailEntity> searchByPage(@RequestBody DictDetailConditionEntity dictDetailConditionEntity) {
        return dictDetailService.searchByPage(dictDetailConditionEntity);
    }


    /**
     * 添加数据字典详情
     *
     * @param dictDetailEntity 数据字典详情实体
     * @return 影响行数
     */
    @ApiOperation(notes = "添加数据字典详情", value = "添加数据字典详情")
    @PostMapping("/insert")
    public int insert(@RequestBody DictDetailEntity dictDetailEntity) {
        return dictDetailService.insert(dictDetailEntity);
    }

    /**
     * 修改数据字典详情
     *
     * @param dictDetailEntity 数据字典详情实体
     * @return 影响行数
     */
    @ApiOperation(notes = "修改数据字典详情", value = "修改数据字典详情")
    @PostMapping("/update")
    public int update(@RequestBody DictDetailEntity dictDetailEntity) {
        return dictDetailService.update(dictDetailEntity);
    }

    /**
     * 删除数据字典详情
     *
     * @param ids 数据字典详情ID
     * @return 影响行数
     */
    @ApiOperation(notes = "删除数据字典详情", value = "删除数据字典详情")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return dictDetailService.deleteByIds(ids);
    }

    /**
     * 根据条件查询数据字典详情列表
     * @param dictDetailConditionEntity 条件
     * @return 数据字典详情列表
     */
    @ApiOperation(notes = "根据条件查询数据字典详情列表", value = "根据条件查询数据字典详情列表")
    @PostMapping("/searchDictDetail")
    public List<DictDetailEntity> searchDictDetail(@RequestBody @NotNull DictDetailConditionEntity dictDetailConditionEntity) {
        return dictDetailService.searchDictDetailFromCache(dictDetailConditionEntity);
    }
}
