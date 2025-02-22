package com.net.sparrow.job;

import com.net.sparrow.enums.JobResult;
import com.net.sparrow.util.FillUserUtil;

/**
 * @Author: Sparrow
 * @Description: 定时任务接口
 * @DateTime: 2025/2/22 15:33
 **/
public abstract class BaseJob {

	/**
	 * 执行job方法
	 * @return 返回结果
	 */
	public JobResult run() {
		return run(null);
	}

	public JobResult run(String params) {
		try {
			FillUserUtil.mockCurrentUser();
			return doRun(params);
		}finally {
			FillUserUtil.clearCurrentUser();
		}
	}

	/**
	 * 给子类重写的真正执行job的方法
	 * @param params
	 * @return
	 */
	public abstract JobResult doRun(String params);

}
