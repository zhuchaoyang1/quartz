package com.kedacom.util;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class CronUtil {

    public boolean checkCron(String cronEx) {
        if (StringUtils.isEmpty(cronEx)) {
            return false;
        }
        try {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronEx.trim());
            return true;
        } catch (Exception e) {
            log.error("\n Cron表达式错误{}", cronEx);
        }
        return false;
    }

}
