package com.net.sparrow.service.task;

import cn.hutool.json.JSONUtil;
import com.net.sparrow.annotation.AsyncTask;
import com.net.sparrow.entity.common.CommonTaskEntity;
import com.net.sparrow.entity.email.RemoteLoginEmailEntity;
import com.net.sparrow.enums.TaskStatusEnum;
import com.net.sparrow.enums.TaskTypeEnum;
import com.net.sparrow.mapper.common.CommonTaskMapper;
import com.net.sparrow.service.email.RemoteLoginEmailService;
import com.net.sparrow.util.FillUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.net.sparrow.constant.NumberConstant.NUMBER_3;

/**
 * 发生邮件服务
 */
@AsyncTask(TaskTypeEnum.SEND_EMAIL)
@Slf4j
@Service
public class SendEmailTask implements IAsyncTask {


    @Autowired
    private CommonTaskMapper commonTaskMapper;
    @Autowired
    private RemoteLoginEmailService remoteLoginEmailService;


    @Override
    public void doTask(CommonTaskEntity commonTaskEntity) {
        doSendEmail(commonTaskEntity);
    }


    private void doSendEmail(CommonTaskEntity commonTaskEntity) {
        //任务开始执行时，状态改成执行中
        commonTaskEntity.setStatus(TaskStatusEnum.RUNNING.getValue());
        FillUserUtil.fillUpdateUserInfoFromCreate(commonTaskEntity);
        commonTaskMapper.update(commonTaskEntity);

        try {
            RemoteLoginEmailEntity remoteLoginEmailEntity = JSONUtil.toBean(commonTaskEntity.getRequestParam(), RemoteLoginEmailEntity.class);
            remoteLoginEmailService.sendEmail(remoteLoginEmailEntity);
            commonTaskEntity.setStatus(TaskStatusEnum.SUCCESS.getValue());
        } catch (Exception e) {
            log.error("数据导出异常，原因：", e);
            //失败次数加1
            commonTaskEntity.setFailureCount(commonTaskEntity.getFailureCount() + 1);
            //如果失败次数超过3次，则将状态改成失败，后面不再执行
            if (commonTaskEntity.getFailureCount() >= NUMBER_3) {
                commonTaskEntity.setStatus(TaskStatusEnum.FAIL.getValue());
            }
        }

        commonTaskEntity.setUpdateTime(new Date());
        commonTaskMapper.update(commonTaskEntity);
    }

}
