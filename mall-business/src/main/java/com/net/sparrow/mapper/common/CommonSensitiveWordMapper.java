package com.net.sparrow.mapper.common;


import com.net.sparrow.entity.common.CommonSensitiveWordConditionEntity;
import com.net.sparrow.entity.common.CommonSensitiveWordEntity;
import com.net.sparrow.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 敏感词 mapper
 */
public interface CommonSensitiveWordMapper extends BaseMapper<CommonSensitiveWordEntity, CommonSensitiveWordConditionEntity> {
    /**
     * 查询敏感词信息
     *
     * @param id 敏感词ID
     * @return 敏感词信息
     */
    CommonSensitiveWordEntity findById(Long id);

    /**
     * 添加敏感词
     *
     * @param commonSensitiveWordEntity 敏感词信息
     * @return 结果
     */
    int insert(CommonSensitiveWordEntity commonSensitiveWordEntity);

    /**
     * 批量添加敏感词
     *
     * @param list 敏感词信息
     * @return 结果
     */
    int batchInsert(List<CommonSensitiveWordEntity> list);

    /**
     * 修改敏感词
     *
     * @param commonSensitiveWordEntity 敏感词信息
     * @return 结果
     */
    int update(CommonSensitiveWordEntity commonSensitiveWordEntity);

    /**
     * 批量删除敏感词
     *
     * @param ids    id集合
     * @param entity 敏感词实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CommonSensitiveWordEntity entity);

    /**
     * 批量查询敏感词信息
     *
     * @param ids ID集合
     * @return 部门信息
     */
    List<CommonSensitiveWordEntity> findByIds(List<Long> ids);
}
