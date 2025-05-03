package com.net.sparrow.service.order;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.order.TradeItemConditionEntity;
import com.net.sparrow.entity.order.TradeItemEntity;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.mapper.order.TradeItemMapper;
import com.net.sparrow.service.BaseService;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 订单明细 服务层
 */
@Service
public class TradeItemService extends BaseService< TradeItemEntity,  TradeItemConditionEntity> {

	@Autowired
	private TradeItemMapper tradeItemMapper;

	/**
     * 查询订单明细信息
     *
     * @param id 订单明细ID
     * @return 订单明细信息
     */
	public TradeItemEntity findById(Long id) {
	    return tradeItemMapper.findById(id);
	}

	/**
     * 根据条件分页查询订单明细列表
     *
     * @param tradeItemConditionEntity 订单明细信息
     * @return 订单明细集合
     */
	public ResponsePageEntity<TradeItemEntity> searchByPage(TradeItemConditionEntity tradeItemConditionEntity) {
		int count = tradeItemMapper.searchCount(tradeItemConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(tradeItemConditionEntity);
		}
		List<TradeItemEntity> dataList = tradeItemMapper.searchByCondition(tradeItemConditionEntity);
		return ResponsePageEntity.build(tradeItemConditionEntity, count, dataList);
	}

    /**
     * 新增订单明细
     *
     * @param tradeItemEntity 订单明细信息
     * @return 结果
     */
	public int insert(TradeItemEntity tradeItemEntity) {
	    return tradeItemMapper.insert(tradeItemEntity);
	}

	/**
     * 修改订单明细
     *
     * @param tradeItemEntity 订单明细信息
     * @return 结果
     */
	public int update(TradeItemEntity tradeItemEntity) {
	    return tradeItemMapper.update(tradeItemEntity);
	}

	/**
     * 批量删除订单明细对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<TradeItemEntity> entities = tradeItemMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "订单明细已被删除");

		TradeItemEntity entity = new TradeItemEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return tradeItemMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return tradeItemMapper;
	}

}
