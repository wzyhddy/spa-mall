package com.net.sparrow.config.properties;

import lombok.Data;

@Data
public class QuartzThreadPoolProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = 8;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 10;

    /**
     * 队列大小
     */
    private int queueSize = 200;

    /**
     * 空闲线程回收时间，多少秒
     */
    private int keepAliveSeconds = 30;

    /**
     * 线程前缀
     */
    private String threadNamePrefix = "QuartzThread";
}