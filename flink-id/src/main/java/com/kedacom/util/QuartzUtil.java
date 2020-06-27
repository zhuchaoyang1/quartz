package com.kedacom.util;

import com.kedacom.bean.Management;
import com.kedacom.bean.QuartzJobDetailTriggerBean;
import com.kedacom.contast.JobClassContanst;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class QuartzUtil {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private CronUtil cronUtil;

    @Autowired
    private Management management;

    @Value("${computed.times:10}")
    private String times;


    /**
     * 获取所有Job
     *
     * @return
     */
    public List<JobDetail> queryAllJobDetail() {
        List<JobDetail> jobDetails = new ArrayList<>();
        Set<JobKey> jobKeys = new HashSet<>();
        try {
            jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
            for (JobKey jobKey : jobKeys) {
                jobDetails.add(scheduler.getJobDetail(jobKey));
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobDetails;
    }

    /**
     * 根据Name、GroupName唯一获取一个Job
     *
     * @param name
     * @param groupName
     * @return
     */
    public JobDetail getJobDetailByKey(String name, String groupName) {
        JobKey jobKey = new JobKey(name, groupName);
        try {
            return scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Job 计算下面X次的执行时间
     *
     * @return
     */
    public List<String> jobCronDates(QuartzJobDetailTriggerBean quartzJobDetailTriggerBean) {
        JobKey jobKey = JobKey.jobKey(quartzJobDetailTriggerBean.getJobName(),
                quartzJobDetailTriggerBean.getJobGroupName());
        List<Date> dates = new ArrayList<>();
        List<String> nextDateTimes = new ArrayList<>();
        try {
            if (scheduler.checkExists(jobKey)) {
                TriggerKey triggerKey = TriggerKey.triggerKey(quartzJobDetailTriggerBean.getTriggerName(),
                        quartzJobDetailTriggerBean.getTriggerGroupName());
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

                if (triggerKey == null || trigger == null) return new ArrayList<>();

                CronTriggerImpl cronTrigger = new CronTriggerImpl();
                cronTrigger.setCronExpression(trigger.getCronExpression());

                dates = TriggerUtils.computeFireTimes(cronTrigger, null, Integer.parseInt(times));
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dates.stream().forEach(var -> nextDateTimes.add(simpleDateFormat.format(var)));

        return nextDateTimes;
    }


    /**
     * Start Or Stop One Job
     * Important 基于数据库，系统重启状态不丢失
     *
     * @param flag      true: start  false: stop
     * @param name
     * @param groupName
     * @return
     */
    public boolean stopOrStart(boolean flag, String name, String groupName) {
        JobKey jobKey = JobKey.jobKey(name, groupName);
        try {
            if (scheduler.checkExists(jobKey)) {
                if (flag) {
                    scheduler.resumeJob(jobKey);
                } else {
                    scheduler.pauseJob(jobKey);
                }
                return true;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkHasJob(String name, String groupName) {
        JobKey jobKey = JobKey.jobKey(name, groupName);
        try {
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean checkHasTrigger(String triggerName, String triggerGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try {
            return scheduler.checkExists(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 添加任务
     *
     * @param quartzJobDetailTriggerBean
     * @return
     */
    public Map<String, Object> addJob(QuartzJobDetailTriggerBean quartzJobDetailTriggerBean) {
        Map<String, Object> result = new HashMap<>();

        // 验证Job
        boolean flagHasJob = this.checkHasJob(quartzJobDetailTriggerBean.getJobName(), quartzJobDetailTriggerBean.getJobGroupName());
        // 验证Cron
        boolean flagCronEx = cronUtil.checkCron(quartzJobDetailTriggerBean.getCronExpression());
        // 验证Trigger
        boolean flagTrigger = this.checkHasTrigger(quartzJobDetailTriggerBean.getTriggerName(), quartzJobDetailTriggerBean.getTriggerGroupName());

        if (flagHasJob) {
            result.put("flag", false);
            result.put("result", "Job已存在");
        }

        if (flagTrigger) {
            result.put("flag", false);
            result.put("result", "触发器已存在");
        }

        if (!flagCronEx) {
            result.put("flag", false);
            result.put("result", "Cron表达式不合法，详见Cron工具：https://cron.qqe2.com/");
        }

        if (!flagTrigger && !flagHasJob && flagCronEx) {
            try {
                // 方便后期Job Class扩展
                if (quartzJobDetailTriggerBean.getJobClass() == null) {
                    quartzJobDetailTriggerBean.setJobClass(Class.forName(JobClassContanst._COMMJOBCLASS));
                }

                management.addJobAndRun(quartzJobDetailTriggerBean);

                result.put("flag", true);
                result.put("result", "创建成功");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        result.put("flag", false);
        if (!result.containsKey("result")) {
            result.put("result", "创建失败");
        }

        return result;
    }

    public boolean deleteJob(String name, String groupName) {
        boolean exists = this.checkHasJob(name, groupName);
        if (exists) {
            try {
                scheduler.deleteJob(JobKey.jobKey(name, groupName));
                return true;
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 修改Job Cron
     *
     * @return
     */
    public boolean updateJobCron(QuartzJobDetailTriggerBean quartzJobDetailTriggerBean) {
        boolean hasJob = this.checkHasJob(quartzJobDetailTriggerBean.getJobName(), quartzJobDetailTriggerBean.getJobGroupName());
        if (hasJob) {
            try {
                TriggerKey triggerKey = TriggerKey.triggerKey(quartzJobDetailTriggerBean.getTriggerName(), quartzJobDetailTriggerBean.getTriggerGroupName());
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                if (trigger == null) return false;
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                triggerBuilder.withIdentity(quartzJobDetailTriggerBean.getTriggerName(), quartzJobDetailTriggerBean.getTriggerGroupName());
                triggerBuilder.startNow();  // TODO 对于Cron中  这句话并没有任何作用
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(quartzJobDetailTriggerBean.getCronExpression()));
                trigger = (CronTrigger) triggerBuilder.build();

                scheduler.rescheduleJob(triggerKey, trigger);
                return true;
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
