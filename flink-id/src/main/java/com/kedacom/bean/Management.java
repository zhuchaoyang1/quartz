package com.kedacom.bean;

import com.kedacom.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class Management {

    @Autowired
    private Scheduler scheduler;

    /**
     * Cron Trigger
     *
     * @param quartzJobDetailTriggerBean
     * @throws Exception
     */
    public void addJobAndRun(QuartzJobDetailTriggerBean quartzJobDetailTriggerBean) throws Exception {
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(quartzJobDetailTriggerBean.getJobClass()).
                withIdentity(quartzJobDetailTriggerBean.getJobName(), quartzJobDetailTriggerBean.getJobGroupName())
                .usingJobData(new JobDataMap(quartzJobDetailTriggerBean.getPayload()))  // JobDataMap构造函数支持Map
                .build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJobDetailTriggerBean.getCronExpression());
        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().
                withIdentity(quartzJobDetailTriggerBean.getTriggerName(), quartzJobDetailTriggerBean.getTriggerGroupName())
                .withSchedule(scheduleBuilder)
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("任务{}创建失败", quartzJobDetailTriggerBean.getJobName());
            e.printStackTrace();
            throw new Exception("Error");
        }
    }

    /**
     * SimpleTrigger触发器
     * TODO 方案中暂时放弃
     *
     * @param quartzJobDetailTriggerBean
     * @throws Exception
     */
    public void addSimpleTriggerAndRun(QuartzJobDetailTriggerBean quartzJobDetailTriggerBean) throws Exception {
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(quartzJobDetailTriggerBean.getJobClass()).
                withIdentity(quartzJobDetailTriggerBean.getJobName(), quartzJobDetailTriggerBean.getJobGroupName())
                .usingJobData(new JobDataMap(quartzJobDetailTriggerBean.getPayload()))  // JobDataMap构造函数支持Map
                .build();

        //构建SimpleTrigger触发器
//        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
//                .withIdentity(quartzJobDetailTriggerBean.getTriggerName(),
//                        quartzJobDetailTriggerBean.getTriggerGroupName())
//                .startAt(new Date())
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(1))
//                .build();

        // 构造任务开始时间
        String startDateStr = quartzJobDetailTriggerBean.getSimpleTriggerTypeBean().getStartDateStr();
        Date startDate = StringUtils.isEmpty(startDateStr) ? new Date() : TimeUtil.natureDateStr2Date(startDateStr);

        // 构造任务结束时间
        String endDateStr = quartzJobDetailTriggerBean.getSimpleTriggerTypeBean().getEndDateStr();
        Date endDate = StringUtils.isEmpty(endDateStr) ? new Date() : TimeUtil.natureDateStr2Date(endDateStr);

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();

        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzJobDetailTriggerBean.getTriggerName(),
                        quartzJobDetailTriggerBean.getTriggerGroupName())
                .startAt(startDate)
                .withSchedule(simpleScheduleBuilder)
                .build();

        try {
            scheduler.scheduleJob(jobDetail, simpleTrigger);
        } catch (SchedulerException e) {
            log.error("任务{}创建失败", quartzJobDetailTriggerBean.getJobName());
            throw new Exception("Error");
        }
    }

}
