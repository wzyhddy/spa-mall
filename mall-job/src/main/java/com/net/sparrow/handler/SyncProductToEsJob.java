package com.net.sparrow.handler;

import com.net.sparrow.enums.JobResult;
import com.net.sparrow.service.es.SyncProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 同步商品数据ES
 * @author 27837
 */
@Slf4j
@Component
public class SyncProductToEsJob extends BaseJob {

    @Autowired
    private SyncProductService syncProductService;

    @Override
    public JobResult doRun(String params) {
        syncProductService.syncProductToES();
        return JobResult.SUCCESS;
    }
}