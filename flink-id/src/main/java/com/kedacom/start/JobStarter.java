//package com.kedacom.start;
//
//import com.kedacom.bean.Management;
//import com.kedacom.bean.QuartzJobDetailTriggerBean;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//public class JobStarter {
//
//    @Autowired
//    private Management management;
//
//    @PostConstruct
//    public void buildJobStart() {
//        log.info("\n日志告警定时任务已开始长跑");
//        try {
//            management.addJobAndRun(this.buildEmailJob());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 邮件任务构建
//    private QuartzJobDetailTriggerBean buildEmailJob() {
//        QuartzJobDetailTriggerBean quartzJobDetailTriggerBean = new QuartzJobDetailTriggerBean();
//        quartzJobDetailTriggerBean.setJobName(EmailConstant._EMAILJOBNAME);
//        quartzJobDetailTriggerBean.setJobGroupName(EmailConstant._EMAILJOBGROUPNAME);
//        quartzJobDetailTriggerBean.setTriggerName(EmailConstant._EMAILTRIGGERNAME);
//        quartzJobDetailTriggerBean.setTriggerGroupName(EmailConstant._EMAILTRIGGERGROUPNAME);
//        quartzJobDetailTriggerBean.setJobClass(EmailConstant._EMAILJOBCLASS);
//        quartzJobDetailTriggerBean.setCronExpression(emailCornExpresion);
//        Map<Object, String> map = new HashMap<>();
//        map.put("cron", emailCornExpresion);
//        quartzJobDetailTriggerBean.setPayload(map);
//        return quartzJobDetailTriggerBean;
//    }
//
//
//}
