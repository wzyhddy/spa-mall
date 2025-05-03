package com.net.sparrow.quartz;

import com.net.sparrow.entity.common.CommonJobConditionEntity;
import com.net.sparrow.entity.common.CommonJobEntity;
import com.net.sparrow.mapper.common.CommonJobMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目启动的时候加载
 */
@Slf4j
@Component
public class QuartzJobRunner implements ApplicationRunner {

    @Autowired
    private CommonJobMapper commonJobMapper;
    @Autowired
    private QuartzManage quartzManage;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("--------------------开始注入定时任务---------------------");
        CommonJobConditionEntity commonJobConditionEntity = new CommonJobConditionEntity();
        commonJobConditionEntity.setPauseStatus(0);
        commonJobConditionEntity.setPageSize(0);
        List<CommonJobEntity> commonJobEntities = commonJobMapper.searchByCondition(commonJobConditionEntity);

        if (CollectionUtils.isNotEmpty(commonJobEntities)) {
            for (CommonJobEntity commonJobEntity : commonJobEntities) {
                quartzManage.addJob(commonJobEntity);
            }
        }
        log.info("--------------------定时任务注入完成，注入定时的数量是：{}---------------------", commonJobEntities.size());
    }
}