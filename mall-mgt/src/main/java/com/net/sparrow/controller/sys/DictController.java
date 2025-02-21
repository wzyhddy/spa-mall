package com.net.sparrow.controller.sys;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.sys.DictConditionEntity;
import com.net.sparrow.entity.sys.DictEntity;
import com.net.sparrow.service.sys.DictService;
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
 * 数据字典 接口层
 */
@Api(tags = "数据字典操作", description = "数据字典接口")
@RestController
@RequestMapping("/v1/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 通过id查询数据字典信息
     *
     * @param id 系统ID
     * @return 数据字典信息
     */
    @ApiOperation(notes = "通过id查询数据字典信息", value = "通过id查询数据字典信息")
    @GetMapping("/findById")
    public DictEntity findById(Long id) {
        return dictService.findById(id);
    }

    /**
     * 根据条件查询数据字典列表
     *
     * @param dictConditionEntity 条件
     * @return 数据字典列表
     */
    @ApiOperation(notes = "根据条件查询数据字典列表", value = "根据条件查询数据字典列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<DictEntity> searchByPage(@RequestBody DictConditionEntity dictConditionEntity) {
        return dictService.searchByPage(dictConditionEntity);
    }


    /**
     * 添加数据字典
     *
     * @param dictEntity 数据字典实体
     * @return 影响行数
     */
    @ApiOperation(notes = "添加数据字典", value = "添加数据字典")
    @PostMapping("/insert")
    public int insert(@RequestBody DictEntity dictEntity) {
        return dictService.insert(dictEntity);
    }

    /**
     * 修改数据字典
     *
     * @param dictEntity 数据字典实体
     * @return 影响行数
     */
    @ApiOperation(notes = "修改数据字典", value = "修改数据字典")
    @PostMapping("/update")
    public int update(@RequestBody DictEntity dictEntity) {
        return dictService.update(dictEntity);
    }

    /**
     * 删除数据字典
     *
     * @param ids 数据字典ID
     * @return 影响行数
     */
    @ApiOperation(notes = "删除数据字典", value = "删除数据字典")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return dictService.deleteByIds(ids);
    }
}
