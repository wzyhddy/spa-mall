package com.net.sparrow.job;

import com.net.sparrow.enums.JobResult;
import com.net.sparrow.util.FillUserUtil;

/**
 * 定时任务接口
 */
public abstract class BaseJob {

    /**
     * 执行job方法
     *
     * @return 执行结果
     */
    public JobResult run() {
        return run(null);
    }

    /**
     * 执行job方法
     *
     * @param params 参数
     * @return 执行结果
     */
    public JobResult run(String params) {
        try {
            FillUserUtil.mockCurrentUser();
            return doRun(params);
        } finally {
            FillUserUtil.clearCurrentUser();
        }
    }

    /**
     * 给子类重写的真正执行job的方法
     *
     * @param params 参数
     * @return 返回值
     */
    public abstract JobResult doRun(String params);
}