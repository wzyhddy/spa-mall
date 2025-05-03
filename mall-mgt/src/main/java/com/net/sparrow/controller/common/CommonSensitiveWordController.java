package com.net.sparrow.controller.common;

import com.net.sparrow.annotation.NoLogin;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.common.CommonSensitiveWordConditionEntity;
import com.net.sparrow.entity.common.CommonSensitiveWordEntity;
import com.net.sparrow.service.CommonSensitiveWordService;
import com.net.sparrow.util.AssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 敏感词 接口层
 */
@Api(tags = "敏感词操作", description = "敏感词接口")
@RestController
@RequestMapping("/v1/commonSensitiveWord")
public class CommonSensitiveWordController {

    @Autowired
    private CommonSensitiveWordService commonSensitiveWordService;

    /**
     * 校验敏感词
     *
     * @param commonSensitiveWordEntity 条件
     * @return 敏感词信息
     */
    @NoLogin
    @ApiOperation(notes = "校验敏感词", value = "校验敏感词")
    @PostMapping("/checkSensitiveWord")
    public void checkSensitiveWord(@RequestBody CommonSensitiveWordEntity commonSensitiveWordEntity) {
        AssertUtil.isTrue(StringUtils.hasLength(commonSensitiveWordEntity.getWord()), "word字段不能为空");
        commonSensitiveWordService.checkSensitiveWord(commonSensitiveWordEntity.getWord());
    }


    /**
     * 通过id查询敏感词信息
     *
     * @param id 系统ID
     * @return 敏感词信息
     */
    @ApiOperation(notes = "通过id查询敏感词信息", value = "通过id查询敏感词信息")
    @GetMapping("/findById")
    public CommonSensitiveWordEntity findById(Long id) {
        return commonSensitiveWordService.findById(id);
    }

    /**
     * 根据条件查询敏感词列表
     *
     * @param commonSensitiveWordConditionEntity 条件
     * @return 敏感词列表
     */
    @ApiOperation(notes = "根据条件查询敏感词列表", value = "根据条件查询敏感词列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<CommonSensitiveWordEntity> searchByPage(@RequestBody CommonSensitiveWordConditionEntity commonSensitiveWordConditionEntity) {
        return commonSensitiveWordService.searchByPage(commonSensitiveWordConditionEntity);
    }


    /**
     * 添加敏感词
     *
     * @param commonSensitiveWordEntity 敏感词实体
     * @return 影响行数
     */
    @ApiOperation(notes = "添加敏感词", value = "添加敏感词")
    @PostMapping("/insert")
    public int insert(@RequestBody CommonSensitiveWordEntity commonSensitiveWordEntity) {
        return commonSensitiveWordService.insert(commonSensitiveWordEntity);
    }

    /**
     * 修改敏感词
     *
     * @param commonSensitiveWordEntity 敏感词实体
     * @return 影响行数
     */
    @ApiOperation(notes = "修改敏感词", value = "修改敏感词")
    @PostMapping("/update")
    public int update(@RequestBody CommonSensitiveWordEntity commonSensitiveWordEntity) {
        return commonSensitiveWordService.update(commonSensitiveWordEntity);
    }

    /**
     * 批量删除敏感词
     *
     * @param ids 敏感词ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除敏感词", value = "批量删除敏感词")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return commonSensitiveWordService.deleteByIds(ids);
    }
}
