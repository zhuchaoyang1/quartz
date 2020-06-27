package com.kedacom.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Quartz Trigger、JobDetail属性封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleTriggerTypeBean {

    private String startDateStr;

    private String endDateStr;

    /**
     * 执行次数
     * -1 表示永远执行
     */
    private Integer repeatCount;

    /**
     * 规则：按照小时
     */
    private Integer repeatHours;

    /**
     * 规则：按照毫秒
     */
    private Integer milliSeconds;

    /**
     * 规则：按照分钟
     */
    private Integer minutes;

    /**
     * 规则：按照秒数
     */
    private Integer seconds;
}
