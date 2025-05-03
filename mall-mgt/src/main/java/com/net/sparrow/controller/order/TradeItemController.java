package com.net.sparrow.controller.order;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.order.TradeItemConditionEntity;
import com.net.sparrow.entity.order.TradeItemEntity;
import com.net.sparrow.service.order.TradeItemService;
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
 * 订单明细 接口层
 */
@Api(tags = "订单明细操作", description = "订单明细接口")
@RestController
@RequestMapping("/v1/tradeItem")
public class TradeItemController {

	@Autowired
	private TradeItemService tradeItemService;

	/**
	 * 通过id查询订单明细信息
	 *
	 * @param id 系统ID
	 * @return 订单明细信息
	 */
	@ApiOperation(notes = "通过id查询订单明细信息", value = "通过id查询订单明细信息")
	@GetMapping("/findById")
	public TradeItemEntity findById(Long id) {
		return tradeItemService.findById(id);
	}

	/**
    * 根据条件查询订单明细列表
    *
    * @param tradeItemConditionEntity 条件
    * @return 订单明细列表
    */
	@ApiOperation(notes = "根据条件查询订单明细列表", value = "根据条件查询订单明细列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<TradeItemEntity> searchByPage(@RequestBody TradeItemConditionEntity tradeItemConditionEntity) {
		return tradeItemService.searchByPage(tradeItemConditionEntity);
	}


	/**
     * 添加订单明细
     *
     * @param tradeItemEntity 订单明细实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加订单明细", value = "添加订单明细")
	@PostMapping("/insert")
	public int insert(@RequestBody TradeItemEntity tradeItemEntity) {
		return tradeItemService.insert(tradeItemEntity);
	}

	/**
     * 修改订单明细
     *
     * @param tradeItemEntity 订单明细实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改订单明细", value = "修改订单明细")
	@PostMapping("/update")
	public int update(@RequestBody TradeItemEntity tradeItemEntity) {
		return tradeItemService.update(tradeItemEntity);
	}

	/**
     * 批量删除订单明细
     *
     * @param ids 订单明细ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除订单明细", value = "批量删除订单明细")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return tradeItemService.deleteByIds(ids);
	}
}
