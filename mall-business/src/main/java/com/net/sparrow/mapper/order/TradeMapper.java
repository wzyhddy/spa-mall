package com.net.sparrow.mapper.order;

import com.net.sparrow.entity.order.TradeConditionEntity;
import com.net.sparrow.entity.order.TradeEntity;
import com.net.sparrow.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单 mapper
 */
public interface TradeMapper extends BaseMapper<TradeEntity, TradeConditionEntity> {
	/**
     * 查询订单信息
     *
     * @param id 订单ID
     * @return 订单信息
     */
	TradeEntity findById(Long id);

	/**
     * 添加订单
     *
     * @param tradeEntity 订单信息
     * @return 结果
     */
	int insert(TradeEntity tradeEntity);

	/**
     * 修改订单
     *
     * @param tradeEntity 订单信息
     * @return 结果
     */
	int update(TradeEntity tradeEntity);

	/**
     * 批量删除订单
     *
     * @param ids id集合
     * @param entity 订单实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") TradeEntity entity);

	/**
     * 批量查询订单信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<TradeEntity> findByIds(List<Long> ids);
}
