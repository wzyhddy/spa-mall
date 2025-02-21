package com.net.sparrow.mapper.sys;

import com.net.sparrow.entity.sys.DictConditionEntity;
import com.net.sparrow.entity.sys.DictEntity;
import com.net.sparrow.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据字典 mapper
 */
public interface DictMapper extends BaseMapper<DictEntity, DictConditionEntity> {
    /**
     * 查询数据字典信息
     *
     * @param id 数据字典ID
     * @return 数据字典信息
     */
    DictEntity findById(Long id);

    /**
     * 根据条件查询数据字典列表
     *
     * @param dictConditionEntity 数据字典信息
     * @return 数据字典集合
     */
    List<DictEntity> searchByCondition(DictConditionEntity dictConditionEntity);

    /**
     * 根据条件查询数据字典数量
     *
     * @param dictConditionEntity 数据字典信息
     * @return 数据字典集合
     */
    int searchCount(DictConditionEntity dictConditionEntity);

    /**
     * 添加数据字典
     *
     * @param dictEntity 数据字典信息
     * @return 结果
     */
    int insert(DictEntity dictEntity);

    /**
     * 修改数据字典
     *
     * @param dictEntity 数据字典信息
     * @return 结果
     */
    int update(DictEntity dictEntity);

    /**
     * 删除数据字典
     *
     * @param id 数据字典ID
     * @return 结果
     */
    int deleteById(Long id);


    /**
     * 批量查询数据字典信息
     *
     * @param ids ID集合
     * @return 数据字典信息
     */
    List<DictEntity> findByIds(List<Long> ids);

    /**
     * 删除数据字典
     *
     * @param ids        id集合
     * @param dictEntity 数据字段实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("dictEntity") DictEntity dictEntity);

}
