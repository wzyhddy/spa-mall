
package com.net.sparrow.job;

import com.net.sparrow.enums.JobResult;
import com.net.sparrow.service.sys.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DictJob extends BaseJob {


    @Autowired
    private DictService dictService;

    @Override
    public JobResult doRun(String params) {
        dictService.refreshDict();
        return JobResult.SUCCESS;
    }

//    @Scheduled(fixedRate = 300000)
//    public void run() {
//        dictService.refreshDict();
//    }


}