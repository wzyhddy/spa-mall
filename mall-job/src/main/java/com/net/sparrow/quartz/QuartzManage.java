package com.net.sparrow.quartz;

import com.net.sparrow.entity.common.CommonJobEntity;
import com.net.sparrow.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 动态定时任务管理器
 */
@Slf4j
@Component
public class QuartzManage {

    public static final String JOB_KEY = "JOB_KEY";
    private static final String JOB_NAME = "TASK_";

    @Autowired
    private Scheduler scheduler;


    public void addJob(CommonJobEntity jobEntity) {
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(QuartzExecutionJob.class).
                    withIdentity(JOB_NAME + jobEntity.getId()).build();

            //通过触发器名和cron 表达式创建 Trigger
            Trigger cronTrigger = newTrigger()
                    .withIdentity(JOB_NAME + jobEntity.getId())
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobEntity.getCronExpression()))
                    .build();

            cronTrigger.getJobDataMap().put(JOB_KEY, jobEntity);

            //重置启动时间
            ((CronTriggerImpl) cronTrigger).setStartTime(new Date());

            //执行定时任务
            scheduler.scheduleJob(jobDetail, cronTrigger);

            // 暂停任务
            if (jobEntity.getPauseStatus()) {
                pauseJob(jobEntity);
            }
        } catch (Exception e) {
            log.error("创建定时任务失败", e);
            throw new BusinessException("创建定时任务失败");
        }
    }

    /**
     * 更新job cron表达式
     *
     * @param jobEntity /
     */
    public void updateJobCron(CommonJobEntity jobEntity) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + jobEntity.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(jobEntity);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobEntity.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置启动时间
            ((CronTriggerImpl) trigger).setStartTime(new Date());
            trigger.getJobDataMap().put(JOB_KEY, jobEntity);

            scheduler.rescheduleJob(triggerKey, trigger);
            // 暂停任务
            if (jobEntity.getPauseStatus()) {
                pauseJob(jobEntity);
            }
        } catch (Exception e) {
            log.error("更新定时任务失败", e);
            throw new BusinessException("更新定时任务失败");
        }

    }

    /**
     * 删除一个job
     *
     * @param jobEntity /
     */
    public void deleteJob(CommonJobEntity jobEntity) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + jobEntity.getId());
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            log.error("删除定时任务失败", e);
            throw new BusinessException("删除定时任务失败");
        }
    }

    /**
     * 恢复一个job
     *
     * @param jobEntity /
     */
    public void resumeJob(CommonJobEntity jobEntity) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + jobEntity.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(jobEntity);
            }
            JobKey jobKey = JobKey.jobKey(JOB_NAME + jobEntity.getId());
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            log.error("恢复定时任务失败", e);
            throw new BusinessException("恢复定时任务失败");
        }
    }

    /**
     * 立即执行job
     *
     * @param jobEntity /
     */
    public void runJobNow(CommonJobEntity jobEntity) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + jobEntity.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(jobEntity);
            }
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(JOB_KEY, jobEntity);
            JobKey jobKey = JobKey.jobKey(JOB_NAME + jobEntity.getId());
            scheduler.triggerJob(jobKey, dataMap);
        } catch (Exception e) {
            log.error("定时任务执行失败", e);
            throw new BusinessException("定时任务执行失败");
        }
    }

    /**
     * 暂停一个job
     *
     * @param jobEntity /
     */
    public void pauseJob(CommonJobEntity jobEntity) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + jobEntity.getId());
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            log.error("定时任务暂停失败", e);
            throw new BusinessException("定时任务暂停失败");
        }
    }
}
