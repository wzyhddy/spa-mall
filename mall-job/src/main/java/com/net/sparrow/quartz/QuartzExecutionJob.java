package com.net.sparrow.quartz;

import com.net.sparrow.entity.common.CommonJobEntity;
import com.net.sparrow.entity.common.CommonJobLogEntity;
import com.net.sparrow.enums.RunStatusEnum;
import com.net.sparrow.service.common.CommonJobLogService;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.util.SpringBeanUtil;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 真正执行定时任务
 */
public class QuartzExecutionJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            FillUserUtil.mockCurrentUser();
            CommonJobEntity jobEntity = (CommonJobEntity) context.getMergedJobDataMap().get(QuartzManage.JOB_KEY);
            // 获取spring bean
            ThreadPoolExecutor quartzThreadPoolExecutor = SpringBeanUtil.getBean("quartzThreadPoolExecutor");
            CommonJobLogService commonJobLogService = SpringBeanUtil.getBean(CommonJobLogService.class);

            CommonJobLogEntity commonJobLogEntity = new CommonJobLogEntity();
            commonJobLogEntity.setJobId(jobEntity.getId());
            commonJobLogEntity.setJobName(jobEntity.getJobName());
            commonJobLogEntity.setBeanName(jobEntity.getBeanName());
            commonJobLogEntity.setMethodName(jobEntity.getMethodName());
            commonJobLogEntity.setParams(jobEntity.getParams());
            commonJobLogEntity.setCronExpression(jobEntity.getCronExpression());
            logger.info("任务准备执行，任务名称：{}", jobEntity.getJobName());
            commonJobLogEntity.setStartTime(new Date());
            commonJobLogEntity.setRunStatus(RunStatusEnum.RUNNING.getValue());
            commonJobLogService.insert(commonJobLogEntity);

            try {
                QuartzTask task = new QuartzTask(jobEntity.getBeanName(),
                        jobEntity.getMethodName(),
                        jobEntity.getParams());
                Future<?> future = quartzThreadPoolExecutor.submit(task);
                future.get();
                // 任务状态
                commonJobLogEntity.setRunStatus(RunStatusEnum.SUCCESS.getValue());
            } catch (Exception e) {
                logger.error("任务执行失败，任务名称：{}", jobEntity.getJobName(), e);
                // 任务状态 0：成功 1：失败
                commonJobLogEntity.setRunStatus(RunStatusEnum.FAILURE.getValue());
                commonJobLogEntity.setException(e.toString());
                jobEntity.setPauseStatus(false);
            } finally {
                //更新状态
                commonJobLogEntity.setEndTime(new Date());
                FillUserUtil.fillUpdateDefaultUserInfo(commonJobLogEntity);
                commonJobLogService.update(commonJobLogEntity);

                if (RunStatusEnum.SUCCESS.getValue().equals(commonJobLogEntity.getRunStatus())) {
                    long times = commonJobLogEntity.getEndTime().getTime() - commonJobLogEntity.getStartTime().getTime();
                    logger.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", jobEntity.getJobName(), times);
                }
            }
        } finally {
            FillUserUtil.clearCurrentUser();
        }
    }
}
