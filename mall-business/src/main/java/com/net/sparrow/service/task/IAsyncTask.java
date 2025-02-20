package com.net.sparrow.service.task;

import com.net.sparrow.entity.common.CommonTaskEntity;

public interface IAsyncTask {

    /**
     * 执行定时任务
     *
     * @param commonTaskEntity 数据
     */
    void doTask(CommonTaskEntity commonTaskEntity);
}