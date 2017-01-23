package com.haier.adp.sla.service;


import com.haier.adp.adp_jenkins.JenkinsService;
import com.haier.adp.adp_jenkins.entity.ReturnValue;
import com.haier.adp.jira.MetricJiraService;
import com.haier.adp.jira.dto.StoryTransferInfo;
import com.haier.adp.jira.impl.MetricJiraServiceImpl;
import com.haier.adp.sla.dao.SlaMonitorDAO;
import com.haier.adp.sla.dao.SlaOutageDAO;
import com.haier.adp.sla.dto.SlaMonitorDTO;
import com.haier.adp.sla.dto.SlaMonitorInterfaceDTO;
import com.haier.adp.sla.dto.SlaOutageDTO;
import com.haier.adp.sla.dto.SlaOutageInterfaceDTO;
import com.haier.adp.sla.util.Ideone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Administrator on 2016/12/22.
 */
@Service
public class SlaInterfaceServiceImpl implements SlaInterfaceService{
    @Autowired
    private SlaOutageDAO slaOutageDAO;
    @Autowired
    private SlaMonitorDAO slaMonitorDAO;
    MetricJiraService metricJiraService=new MetricJiraServiceImpl();
    JenkinsService jenkinsService=new JenkinsService();
    /**
     *   @param  almApplicationSCode s码  fromDate 宕机开始时间,toDate 宕机结束时间
     *根据s码和时间段查宕机信息
     * @return
     */
    @Override
    public SlaOutageInterfaceDTO getOutageInfo(String almApplicationSCode,String fromDate,String toDate ) {
        Map map=new HashMap();
        map.put("almApplicationId",almApplicationSCode);
        map.put("fromDate",fromDate);
        map.put("toDate",toDate);
        List<SlaOutageDTO> slaOutageList=new ArrayList<SlaOutageDTO>();
        slaOutageList=slaOutageDAO.getSlaOutageList((map));
        int totalTime = getTotalTime(slaOutageList);
        SlaOutageInterfaceDTO slaOutageInterfaceDTO=new SlaOutageInterfaceDTO();
        slaOutageInterfaceDTO.setTotalTime(totalTime);//宕机时间
        long time=(Timestamp.valueOf(map.get("toDate").toString()).getTime()-Timestamp.valueOf(map.get("fromDate").toString()).getTime());
        double percent=1-(long)totalTime/time;
        slaOutageInterfaceDTO.setPercent(percent*100+"%");//宕机稳定性
        slaOutageInterfaceDTO.setTotalNum(slaOutageList.size());//宕机次数
        //总的扣款人天
        slaOutageInterfaceDTO.setTotalCutPayment((int) slaOutageDAO.getTotalCutPayment(map));
        return slaOutageInterfaceDTO;
    }
    public int getTotalTime(List<SlaOutageDTO> slaOutageList) {
        Ideone ideone=new Ideone();
        int totalTime=0;
        try {

            totalTime = ideone.getTotalTime(slaOutageList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalTime;
    }


    /**
     *   @param  almApplicationSCode s码  fromDate 宕机开始时间,toDate 宕机结束时间
     *根据s码和时间段查宕机信息
     * @return
     */
    @Override
    public SlaMonitorInterfaceDTO getMointorInfo(String almApplicationSCode,String fromDate,String toDate) {
        Map map = new HashMap();
        map.put("almApplicationId", almApplicationSCode);
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        SlaMonitorInterfaceDTO slaMonitorInterfaceDTO = new SlaMonitorInterfaceDTO();
        SlaMonitorDTO slaMonitorDTO = slaMonitorDAO.getPercent(map);
        slaMonitorInterfaceDTO.setInvokedTotal(new Double(slaMonitorDTO.getInvokedTotal()).intValue());//总次数
        slaMonitorInterfaceDTO.setInvokedSuccessTotal(new Double(slaMonitorDTO.getInvokedSuccessTotal()).intValue());//成功次数
        slaMonitorInterfaceDTO.setInvokedFailTotal(new Double(slaMonitorDTO.getInvokedFailTotal()).intValue());//失败次数
        double percent = slaMonitorDTO.getInvokedFailTotal() / slaMonitorDTO.getInvokedTotal();
        slaMonitorInterfaceDTO.setPercent(percent * 100 + "%");//稳定性
        if (percent >= 0.01) {
            slaMonitorInterfaceDTO.setMonitorCutPayment(3);//扣款人天
            slaMonitorInterfaceDTO.setMonitorDeductScore(15);//扣分
        } else {
            slaMonitorInterfaceDTO.setMonitorCutPayment(0);//扣款人天
            slaMonitorInterfaceDTO.setMonitorDeductScore(0);//扣分
        }
        return slaMonitorInterfaceDTO;
    }

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
                ReturnValue rv=jenkinsService.createJobFromPaaS(projectName);
                IfSuccess=rv.isSuccess()==true?"1":"0";
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
                ReturnValue rv = jenkinsService.triggerBuildFromPaaS(projectName);
                IfSuccess = rv.isSuccess() == true ? "1" : "0";
            }else {
                IfSuccess="0";
            }
        } catch (IOException e) {
            e.printStackTrace();
            IfSuccess="0";
        }
        return IfSuccess;
    }

    /**
     *  //根据应用简称almShortName、fromDate 开始时间,toDate 结束时间得到已经关闭的任务列表
     * @param almShortName
     * @param fromDate
     * @param toDate
     * @return
     */
    @Override
    public List<StoryTransferInfo> getStoryList(String almShortName, Date fromDate, Date toDate) {
        List<StoryTransferInfo> storyLists=new ArrayList<StoryTransferInfo>();
        try {
            if(almShortName!=null&&fromDate!=null&&toDate!=null&&
                    !"".equals(almShortName)&&!"".equals(fromDate)&&!"".equals(toDate)){
                storyLists = metricJiraService.getStoryList(almShortName,fromDate,toDate);
            }else{
                storyLists=null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            storyLists=null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            storyLists=null;
        }
        return storyLists;
    }

    /**
     *根据任务id，发版时间更新任务的发版时间
     * @param taskIds
     * @return
     */
    @Override
    public String updateStoryReleaseTime(Map<String, String> taskIds) {
        String IfSuccess="1";
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
