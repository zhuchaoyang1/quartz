package com.kedacom.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * 业务Job共享
 */
@Slf4j
public class CommJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();

        log.info("当前Job：{}", jobKey);

        JobDataMap payLoad = jobExecutionContext.getJobDetail().getJobDataMap();

        // 以下内容为创建Job时自定义保存
        Object cronEx = payLoad.get("cron");
        Object id = payLoad.get("id");
        log.info("载荷信息PayId：{},{}", cronEx, id);

    }

}
