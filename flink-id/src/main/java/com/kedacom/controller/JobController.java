package com.kedacom.controller;

import com.alibaba.fastjson.JSON;
import com.kedacom.bean.QuartzJobDetailTriggerBean;
import com.kedacom.util.QuartzUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 获取所有Job
 */
@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private QuartzUtil quartzUtil;

    /**
     * 获取所有Job
     *
     * @return
     */
    @GetMapping
    public ResponseEntity queryAllJobs() {
        return ResponseEntity.ok(JSON.toJSONString(quartzUtil.queryAllJobDetail()));
    }

    /**
     * 某个Job下X次的执行时间
     *
     * @param quartzJobDetailTriggerBean
     * @return
     */
    @PostMapping
    public ResponseEntity<List<String>> queryJobNextDateTimes(@RequestBody QuartzJobDetailTriggerBean quartzJobDetailTriggerBean) {
        return ResponseEntity.ok(quartzUtil.jobCronDates(quartzJobDetailTriggerBean));
    }

    /**
     * 添加Job
     *
     * @param quartzJobDetailTriggerBean
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addJob(@RequestBody QuartzJobDetailTriggerBean quartzJobDetailTriggerBean) {
        return ResponseEntity.ok(quartzUtil.addJob(quartzJobDetailTriggerBean));
    }

    @GetMapping("/{symbol}/{jobName}/{jobGroupName}")
    public ResponseEntity stopJob(@PathVariable String symbol, @PathVariable String jobName, @PathVariable String jobGroupName) {
        boolean startOrStop = symbol.equals("start") ? true : false;
        startOrStop = quartzUtil.stopOrStart(startOrStop, jobName, jobGroupName);
        return ResponseEntity.ok(startOrStop ? "操作成功" : "操作失败，请考虑Job是否存在");
    }

    @DeleteMapping("/{jobName}/{jobGroupName}")
    public ResponseEntity deleteJob(@PathVariable String jobName, @PathVariable String jobGroupName) {
        boolean falg = quartzUtil.deleteJob(jobName, jobGroupName);
        return ResponseEntity.ok(falg ? "操作成功" : "操作失败，请考虑Job是否存在");
    }

    @PutMapping
    public ResponseEntity updateJobCron(@RequestBody QuartzJobDetailTriggerBean quartzJobDetailTriggerBean) {
        return ResponseEntity.ok(quartzUtil.updateJobCron(quartzJobDetailTriggerBean));
    }


}


