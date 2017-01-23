package com.haier.adp.util;

import com.haier.adp.sla.dto.PaasOutageDTO;
import com.haier.adp.sla.dto.SlaMonitorDTO;
import com.haier.adp.sla.dto.SlaOutageDTO;
import com.haier.adp.sla.dto.SlaProjectInfoDTO;
import com.haier.adp.sla.service.SlaMonitorService;
import com.haier.adp.sla.service.SlaOutageService;
import com.haier.adp.sla.service.SlaProjectInfoService;
import com.haier.profiler.config.DTO.AppProfilerData;
import com.haier.profiler.config.service.ProfilerReadService;
import com.haier.profiler.project.DTO.ProjectInfoData;
import com.haier.profiler.project.service.ProfilerProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/12/22.
 */
@Component
public class Scheduler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SlaOutageService slaOutageService;
    @Autowired
    private ProfilerProjectService profilerProjectService;
    @Autowired
    private ProfilerReadService profilerReadService;
    @Autowired
    private SlaProjectInfoService slaProjectInfoService;
    @Autowired
    private SlaMonitorService slaMonitorService;
    /*@Scheduled(fixedRate = 3000)*///每3秒

    /**
     * 每天定时取宕机数据
     */
    @Scheduled(cron="${com.outage.time}")  //每天早上6点执行一次
    public void statusCheck() {
        logger.info("每天早上6点执行一次。开始……");
        //先获取paas数据
        List<SlaOutageDTO> slaOutageList=new ArrayList<SlaOutageDTO>();
        //取得前一天的日期 yyyy-MM-dd
        Date date=new Date(new Date().getTime()-24*60*60*1000);
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        String time = matter1.format(date);
        Map map=new HashMap();
        //type 0 获取服务信息（实例均宕机）  1根据服务名称查询所有（服务）实例的宕机开始结束时间
        //date 获取paas表中统计日期为前一天的数据
        map.put("type",0);
        map.put("time",time);
        List<PaasOutageDTO> paasOutageList=slaOutageService.getPaasOutageList(map);
        //遍历paasOutageList 即遍历服务
        for(int i=0;i<paasOutageList.size();i++){
            PaasOutageDTO paasOutageDTO=paasOutageList.get(i);
            map.put("type",1);
            map.put("serviceName",paasOutageDTO.getServiceName());
            List<PaasOutageDTO> paasPerOutageList =slaOutageService.getPaasOutageList(map);
            long startTime=0;
            long endTime=0;
            SlaOutageDTO slaOutageDTO = new SlaOutageDTO();
            //遍历开始结束时间，找出宕机时间重合的部分
            for(int j=0;j<paasPerOutageList.size();j++){
                PaasOutageDTO paasOutageDTO1=paasPerOutageList.get(j);
                long downtimeBegin=paasOutageDTO1.getDowntimeBegin().getTime();
                long downtimeEnd=paasOutageDTO1.getDowntimeEnd().getTime();
                if(j==0){
                    startTime=downtimeBegin;
                    endTime=downtimeEnd;
                }else{
                    if(startTime>downtimeBegin){
                        startTime=downtimeBegin;
                    }
                    if(endTime<downtimeEnd){
                        endTime=downtimeEnd;
                    }
                }
                if(j==paasPerOutageList.size()-1) {
                    if (startTime < endTime) {
                        slaOutageDTO.setProjectName(paasOutageDTO.getAppName());
                        slaOutageDTO.setProjectId(paasOutageDTO.getAppId());
                        slaOutageDTO.setServiceName(paasOutageDTO.getServiceName());
                        slaOutageDTO.setCreateTime(new Timestamp(new Date().getTime()));
                        slaOutageDTO.setStatus("0");
                        slaOutageDTO.setIfDel("0");
                        slaOutageDTO.setOutageStartDate(new Timestamp(startTime));
                        slaOutageDTO.setOutageEndDate(new Timestamp(endTime));
                        long num = (downtimeBegin - startTime) / 1000 / 60;
                        slaOutageDTO.setOutageTime((int) num);
                        if (num > 30) {
                            slaOutageDTO.setIfOvertime("1");
                        }
                        if (num >= 30) {
                            slaOutageDTO.setDeductScore(10);
                            slaOutageDTO.setCutPayment(2);
                        }
                        slaOutageDTO.setIfNotRun("0");
                        slaOutageDTO.setAssessStatus("1");
                    }
                }
            }
            //查alm s码  应用简称
            Map mm=new HashMap();
            mm.put("projectName",slaOutageDTO.getProjectName());
            List<SlaProjectInfoDTO> slaProjectInfoDTOList=slaOutageService.getSlaProjectInfoList(mm);
            if(slaProjectInfoDTOList.size()>0){
                SlaProjectInfoDTO slaProjectInfoDTO=slaProjectInfoDTOList.get(0);
                slaOutageDTO.setAlmApplicationId(slaProjectInfoDTO.getAlmApplicationId());
                slaOutageDTO.setAlmShortName(slaProjectInfoDTO.getAlmShortName());
            }
            //执行插入
            slaOutageService.insertSlaOutagelData(slaOutageDTO);
        }
        logger.info("每天早上6点执行一次。结束。");
    }

    /**
     *
     * 每天定时更新项目信息
     */
    @Scheduled(cron="${com.monitor.time}")  //每天早上6点执行一次
    public void statusCheck2() {
        logger.info("每天早上6点执行一次。开始……");
        //先删后插
        slaProjectInfoService.delProjectInfo();
        List<ProjectInfoData> projectInfoDatalist=profilerProjectService.getProjectInfoFromPaaS();
        if(projectInfoDatalist.size()>0){
            for (int i = 0; i <projectInfoDatalist.size() ; i++) {
                ProjectInfoData projectInfoData=projectInfoDatalist.get(i);
                SlaProjectInfoDTO slaProjectInfoDTO=new SlaProjectInfoDTO();
                slaProjectInfoDTO.setAlmApplicationId(projectInfoData.getAlmApplicationId());
                slaProjectInfoDTO.setAlmShortName(projectInfoData.getAlmShortName());
                slaProjectInfoDTO.setProjectId(projectInfoData.getProjectId()+"");
                slaProjectInfoDTO.setProjectName(projectInfoData.getProjectName());
                slaProjectInfoService.insertSlaProjectInfo(slaProjectInfoDTO);
            }
        }
        logger.info("每天早上6点执行一次。结束。");
    }
    /**
     *每天定时取dubbo异常数据
     */
    @Scheduled(cron="${com.project.time}")  //每天早上6点执行一次
    public void statusCheck3() {
        logger.info("每天早上6点执行一次。开始……");
        //根据项目名称、时间段获取dubbo异常信息
        //获得前一天的时间
        Date date=new Date(new Date().getTime()-24*60*60*1000);
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        String time = matter1.format(date);
        Date start_date=null;
        Date end_date=null;
        try {
            start_date = matter1.parse(time+" 00:00:00");
            end_date = matter1.parse(time+" 23:59:59");
            //遍历项目关系表
            List<SlaProjectInfoDTO> slaProjectInfolist=slaProjectInfoService.getSlaProjectInfoList(null);
            if(slaProjectInfolist.size()>0){
                for (int i = 0; i <slaProjectInfolist.size() ; i++) {
                    SlaProjectInfoDTO slap=slaProjectInfolist.get(i);
                    String projectName=slap.getProjectName();
                    List<AppProfilerData> appProlist=profilerReadService.getProfilersByDateAndProjectName(start_date,end_date,projectName);
                    if(appProlist.size()>0){
                        for (int j = 0; j <appProlist.size() ; j++) {
                            //执行插入dubbo信息
                            AppProfilerData appProfilerData=appProlist.get(j);
                            SlaMonitorDTO slaMonitorDTO=new SlaMonitorDTO();
                            slaMonitorDTO.setAppName(appProfilerData.getAppName());
                            slaMonitorDTO.setInvokedTotal((int)appProfilerData.getInvokedTotal());
                            slaMonitorDTO.setInvokedSuccessTotal((int)appProfilerData.getInvokedSuccessTotal());
                            slaMonitorDTO.setInvokedFailTotal((int)appProfilerData.getInvokedFailTotal());
                            slaMonitorDTO.setProjectId(slap.getProjectId());
                            slaMonitorDTO.setProjectName(slap.getProjectName());
                            slaMonitorDTO.setAlmApplicationId(slap.getAlmApplicationId());
                            slaMonitorDTO.setAlmShortName(slap.getAlmShortName());
                            slaMonitorDTO.setCreateTime(new Timestamp(new Date().getTime()));
                            slaMonitorDTO.setMonitorDate(date);
                            slaMonitorDTO.setAssessStatus("1");
                            slaMonitorDTO.setStatus("0");
                            slaMonitorDTO.setIfDel("0");
                            slaMonitorService.insertSlaMonitor(slaMonitorDTO);
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        logger.info("每天早上6点执行一次。结束。");
    }
}
