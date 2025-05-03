package com.net.sparrow.mapper.order;

import com.net.sparrow.entity.order.TradeItemConditionEntity;
import com.net.sparrow.entity.order.TradeItemEntity;
import com.net.sparrow.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单明细 mapper
 */
public interface TradeItemMapper extends BaseMapper<TradeItemEntity, TradeItemConditionEntity> {
    /**
     * 查询订单明细信息
     *
     * @param id 订单明细ID
     * @return 订单明细信息
     */
    TradeItemEntity findById(Long id);

    /**
     * 添加订单明细
     *
     * @param tradeItemEntity 订单明细信息
     * @return 结果
     */
    int insert(TradeItemEntity tradeItemEntity);

    /**
     * 批量添加订单明细
     *
     * @param list
     * @return
     */
    int batchInsert(List<TradeItemEntity> list);

    /**
     * 修改订单明细
     *
     * @param tradeItemEntity 订单明细信息
     * @return 结果
     */
    int update(TradeItemEntity tradeItemEntity);

    /**
     * 批量删除订单明细
     *
     * @param ids    id集合
     * @param entity 订单明细实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") TradeItemEntity entity);

    /**
     * 批量查询订单明细信息
     *
     * @param ids ID集合
     * @return 部门信息
     */
    List<TradeItemEntity> findByIds(List<Long> ids);
}
