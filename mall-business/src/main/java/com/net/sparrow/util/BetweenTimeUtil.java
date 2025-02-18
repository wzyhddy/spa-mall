package com.net.sparrow.util;

import com.net.sparrow.entity.RequestConditionEntity;
import org.apache.commons.collections4.CollectionUtils;

import static com.net.sparrow.constant.NumberConstant.NUMBER_1;
import static com.net.sparrow.constant.NumberConstant.NUMBER_2;


/**
 * 时间范围工具
 */
public abstract class BetweenTimeUtil {

    private BetweenTimeUtil() {
    }

    /**
     * 解析查询条件中的实际范围
     *
     * @param conditionEntity 查询条件
     */
    public static void parseTime(RequestConditionEntity conditionEntity) {
        if (CollectionUtils.isEmpty(conditionEntity.getBetweenTime())) {
            return;
        }

        if (conditionEntity.getBetweenTime().size() == NUMBER_1) {
            conditionEntity.setCreateBeginTime(conditionEntity.getBetweenTime().get(0));
        } else if (conditionEntity.getBetweenTime().size() == NUMBER_2) {
            conditionEntity.setCreateBeginTime(conditionEntity.getBetweenTime().get(0));
            conditionEntity.setCreateEndTime(conditionEntity.getBetweenTime().get(1));
        }
    }
}
