package com.net.sparrow.service.order;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.auth.JwtUserEntity;
import com.net.sparrow.entity.mall.ProductConditionEntity;
import com.net.sparrow.entity.mall.ProductEntity;
import com.net.sparrow.entity.order.TradeConditionEntity;
import com.net.sparrow.entity.order.TradeEntity;
import com.net.sparrow.entity.order.TradeItemEntity;
import com.net.sparrow.enums.OrderStatusEnum;
import com.net.sparrow.enums.PayStatusEnum;
import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.helper.IdGenerateHelper;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.mapper.mall.ProductMapper;
import com.net.sparrow.mapper.order.TradeItemMapper;
import com.net.sparrow.mapper.order.TradeMapper;
import com.net.sparrow.service.BaseService;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.util.OrderCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.net.sparrow.util.OrderCodeUtil.ORDER_CODE_LENGTH;

/**
 * 订单 服务层
 */
@Service
public class TradeService extends BaseService<TradeEntity, TradeConditionEntity> {

    @Autowired
    private TradeMapper tradeMapper;
    @Autowired
    private TradeItemMapper tradeItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private IdGenerateHelper idGenerateHelper;

    /**
     * 查询订单信息
     *
     * @param id 订单ID
     * @return 订单信息
     */
    public TradeEntity findById(Long id) {
        return tradeMapper.findById(id);
    }

    /**
     * 根据条件分页查询订单列表
     *
     * @param tradeConditionEntity 订单信息
     * @return 订单集合
     */
    public ResponsePageEntity<TradeEntity> searchByPage(TradeConditionEntity tradeConditionEntity) {
        int count = tradeMapper.searchCount(tradeConditionEntity);
        if (count == 0) {
            return ResponsePageEntity.buildEmpty(tradeConditionEntity);
        }
        List<TradeEntity> dataList = tradeMapper.searchByCondition(tradeConditionEntity);
        return ResponsePageEntity.build(tradeConditionEntity, count, dataList);
    }

    /**
     * 用户下单
     *
     * @param tradeEntity 订单信息
     * @return 结果
     */
    public void create(TradeEntity tradeEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        checkParam(tradeEntity);
        checkRepeat(tradeEntity);

        tradeEntity.setUserId(currentUserInfo.getId());
        tradeEntity.setUserName(currentUserInfo.getUsername());
        tradeEntity.setCode(getCode(tradeEntity));
        tradeEntity.setOrderStatus(OrderStatusEnum.CREATE.getValue());
        tradeEntity.setPayStatus(PayStatusEnum.WAIT_PAY.getValue());
        tradeEntity.setOrderTime(new Date());

        fillTradeItemEntity(tradeEntity);

        transactionTemplate.execute((status) -> {
                    tradeMapper.insert(tradeEntity);
                    tradeEntity.getTradeItemEntityList().forEach(x -> x.setTradeId(tradeEntity.getId()));
                    tradeItemMapper.batchInsert(tradeEntity.getTradeItemEntityList());
                    return Boolean.TRUE;
                }
        );

    }

    private void fillTradeItemEntity(TradeEntity tradeEntity) {
        List<Long> productIdList = tradeEntity.getTradeItemEntityList()
                .stream().map(TradeItemEntity::getProductId).collect(Collectors.toList());

        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setIdList(productIdList);
        productConditionEntity.setPageSize(0);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
        AssertUtil.notEmpty(productEntities, "商品不能为空");

        List<Long> notFoundList = productIdList.stream().filter(x -> productEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
        AssertUtil.isTrue(org.apache.commons.collections4.CollectionUtils.isEmpty(notFoundList), String.format("商品ID：%s，在系统中不存在", notFoundList));

        for (TradeItemEntity tradeItemEntity : tradeEntity.getTradeItemEntityList()) {
            ProductEntity productEntity = productEntities.stream().filter(x -> x.getId().equals(tradeItemEntity.getProductId()))
                    .findAny().orElseThrow(() -> new BusinessException(String.format("商品ID：%s，在系统中不存在", tradeItemEntity.getProductId())));
            tradeItemEntity.setProductName(productEntity.getName());
            tradeItemEntity.setModel(productEntity.getModel());
            tradeItemEntity.setId(idGenerateHelper.nextId());
        }
    }

    private void checkParam(TradeEntity tradeEntity) {
        AssertUtil.isTrue(checkGtZero(tradeEntity.getTotalAmount()), "总金额必须大于0");
        AssertUtil.isTrue(checkLtZero(tradeEntity.getTotalAmount(), 100000), "总金额必须小于100000");
        AssertUtil.isTrue(checkGtZero(tradeEntity.getPaymentAmount()), "付款金额必须大于0");
        AssertUtil.isTrue(checkLtZero(tradeEntity.getPaymentAmount(), 100000), "总金额必须小于100000");

        for (TradeItemEntity tradeItemEntity : tradeEntity.getTradeItemEntityList()) {
            AssertUtil.isTrue(checkGtZero(tradeItemEntity.getAmount()), "金额必须大于0");
            AssertUtil.isTrue(checkLtZero(tradeItemEntity.getAmount(), 100000), "金额必须小于10000");
            AssertUtil.isTrue(checkGtZero(tradeItemEntity.getPrice()), "单价必须大于0");
            AssertUtil.isTrue(checkLtZero(tradeItemEntity.getPrice(), 100000), "单价必须小于10000");
            AssertUtil.isTrue(tradeItemEntity.getQuantity() > 0, "数量必须大于0");
        }
    }

    private void checkRepeat(TradeEntity tradeEntity) {
        if (!StringUtils.hasLength(tradeEntity.getCode())) {
            return;
        }

        TradeConditionEntity tradeConditionEntity = new TradeConditionEntity();
        tradeConditionEntity.setCode(tradeEntity.getCode());
        List<TradeEntity> tradeEntities = tradeMapper.searchByCondition(tradeConditionEntity);
        AssertUtil.isTrue(CollectionUtils.isEmpty(tradeEntities), "该订单编号已存在");
    }

    private boolean checkGtZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean checkLtZero(BigDecimal value, Integer maxValue) {
        return value.compareTo(new BigDecimal(maxValue)) < 0;
    }

    private String getCode(TradeEntity tradeEntity) {
        if (StringUtils.hasLength(tradeEntity.getCode())) {
            AssertUtil.isTrue(tradeEntity.getCode().length() == ORDER_CODE_LENGTH, String.format("订单号要求必须是%s位的", ORDER_CODE_LENGTH));
            return tradeEntity.getCode();
        }
        return OrderCodeUtil.generateOrderCode();
    }


    /**
     * 修改订单
     *
     * @param tradeEntity 订单信息
     * @return 结果
     */
    public int update(TradeEntity tradeEntity) {
        return tradeMapper.update(tradeEntity);
    }

    /**
     * 批量删除订单对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<TradeEntity> entities = tradeMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "订单已被删除");

        TradeEntity entity = new TradeEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return tradeMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return tradeMapper;
    }

}
