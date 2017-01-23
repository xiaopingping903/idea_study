package com.haier.adp.sla.service;

import com.haier.adp.jira.dto.StoryTransferInfo;
import com.haier.adp.sla.dto.SlaMonitorInterfaceDTO;
import com.haier.adp.sla.dto.SlaOutageInterfaceDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface SlaInterfaceService {
    //根据 almApplicationSCode s码  fromDate 开始时间,toDate 结束时间 得到 宕机时间、宕机稳定性、宕机次数、宕机扣款人天
    SlaOutageInterfaceDTO getOutageInfo(String almApplicationSCode, String fromDate, String toDate);
    //根据 almApplicationSCode s码  fromDate 开始时间,toDate 结束时间 得到 异常总次数、异常失败次数、异常成功次数、稳定性、扣款人天
    SlaMonitorInterfaceDTO getMointorInfo(String almApplicationSCode, String fromDate, String toDate);
    //根据paas项目名创建Jenkins JOb的接口
    String createJenkinsJob(String projectName);
    //根据paas项目名触发执行Jenkins JOb的接口
    String executeJenkinsJob(String projectName);
    //根据应用简称almShortName、fromDate 开始时间,toDate 结束时间得到已经关闭的任务列表
    List<StoryTransferInfo> getStoryList(String almShortName,Date fromDate,Date toDate);
    //根据任务id，发版时间更新任务的发版时间
    String updateStoryReleaseTime(Map<String, String> taskIds);
}
