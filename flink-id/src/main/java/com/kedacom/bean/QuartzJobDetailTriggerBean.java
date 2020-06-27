package com.kedacom.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Quartz Trigger、JobDetail属性封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuartzJobDetailTriggerBean {

    private String jobName;

    private String jobGroupName;

    private String triggerName;

    private String triggerGroupName;

    private Class jobClass;

    private String cronExpression;

    /**
     * 存放在Job中的负载信息
     */
    private Map<Object, String> payload = new HashMap<>();

    /**
     * 按照SimpleTrigger触发器，若为null则按照Cron规则创建触发器
     */
    private SimpleTriggerTypeBean simpleTriggerTypeBean;

}
