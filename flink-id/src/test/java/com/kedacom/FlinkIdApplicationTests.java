package com.kedacom;

import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class FlinkIdApplicationTests {

    @Test
    public void contextLoads() {
        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        try {
            cronTrigger.setCronExpression("0 0 * 4 5 ? 2020-2020");
            TriggerUtils.computeFireTimes(cronTrigger, null, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }






    }

}
