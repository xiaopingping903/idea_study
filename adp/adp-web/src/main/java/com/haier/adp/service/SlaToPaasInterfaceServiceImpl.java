package com.haier.adp.service;

import com.haier.adp.adp_jenkins.JenkinsService;
import com.haier.adp.adp_jenkins.entity.ReturnValue;
import com.haier.adp.jira.MetricJiraService;
import com.haier.adp.jira.impl.MetricJiraServiceImpl;
import com.haier.adp.sla.service.SlaTOPaasInterfaceService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/20.
 */
@Service("slaTOPaasInterfaceService")
public class SlaToPaasInterfaceServiceImpl implements SlaTOPaasInterfaceService {
    MetricJiraService metricJiraService=new MetricJiraServiceImpl();
    JenkinsService jenkinsService=new JenkinsService();
    /**
     *根据paas项目名创建Jenkins JOb的接口
     * @param projectName paas项目名
     * @return
     */
    @Override
    public String createJenkinsJob(String projectName) {
        String IfSuccess="1";
        try {
            if(projectName!=null&&!"".equals(projectName)){
                ReturnValue createJobMsg=jenkinsService.createJobFromPaaS(projectName);
            }else{
                IfSuccess="0";
            }
        }catch (IOException e) {
            e.printStackTrace();
            IfSuccess="0";
        }
        return IfSuccess;
    }

    /**
     *根据paas项目名触发执行Jenkins JOb的接口
     * @param projectName paas项目名
     * @return
     */
    @Override
    public String executeJenkinsJob(String projectName) {
        String IfSuccess="1";
        try {
            if(projectName!=null&&!"".equals(projectName)) {
               ReturnValue responseCode= jenkinsService.triggerBuildFromPaaS(projectName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            IfSuccess="0";
        }
        return IfSuccess;
    }

    /**
     *根据任务id，发版时间更新任务的发版时间
     * @param taskIds
     * @return
     */
    @Override
    public String updateStoryReleaseTime(Map<String, String> taskIds) {
        String IfSuccess="1";
        String str="";
        try {
            if(taskIds!=null&&!"".equals(taskIds)){
                metricJiraService.updateStorylistReleaseTime(taskIds);
            }else{
                IfSuccess="0";
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            IfSuccess="0";
        }
        return IfSuccess;
    }


}
