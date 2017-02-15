package com.haier.adp.sla.service;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/20.
 */
public interface SlaTOPaasInterfaceService {
    //根据paas项目名创建Jenkins JOb的接口
    String createJenkinsJob(String projectName);
    //根据paas项目名触发执行Jenkins JOb的接口
    String executeJenkinsJob(String projectName);
    //根据任务id，发版时间更新任务的发版时间
    String updateStoryReleaseTime(Map<String, String> taskIds);
}
