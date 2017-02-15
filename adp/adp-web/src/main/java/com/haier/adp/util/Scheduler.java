package com.haier.adp.util;

import com.haier.adp.sla.dto.*;
import com.haier.adp.sla.service.SlaALMJIRARelationHistoryService;
import com.haier.adp.sla.service.SlaMonitorService;
import com.haier.adp.sla.service.SlaOutageService;
import com.haier.adp.sla.service.SlaProjectInfoService;
import com.haier.profiler.config.DTO.AppProfilerData;
import com.haier.profiler.config.service.ProfilerReadService;
import com.haier.profiler.project.DTO.ProjectInfoData;
import com.haier.profiler.project.DTO.ProjectUserInfo;
import com.haier.profiler.project.service.ProfilerProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
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
    @Autowired
    private SlaALMJIRARelationHistoryService slaALMJIRARelationHistoryService;
    @Autowired
    JavaMailSender mailSender;

    /**
     *
     * 每天6点定时从paas数据库取宕机信息
     */
    @Scheduled(cron="${com.outage.time}")  //每天早上6点执行一次
    public void testOutage(){
        logger.info("每天早上6点执行一次从paas数据库取宕机信息。开始……");
        Map map=new HashMap();
        map.put("type","0");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date(new Date().getTime()-24*60*60*1000);
        String time=sdf.format(date);
        map.put("time",time);
        //查所有实例均为宕机的服务
        List<PaasOutageDTO> serviceNamelist=slaOutageService.getPaasOutageList(map);
        if(serviceNamelist.size()>0){
            for (int i = 0; i <serviceNamelist.size() ; i++) {
                PaasOutageDTO serviceNameDto=serviceNamelist.get(i);
                SlaOutageDTO dto=new SlaOutageDTO();
                dto.setProjectId(serviceNameDto.getAppId());
                dto.setProjectName(serviceNameDto.getAppName());
                dto.setServiceName(serviceNameDto.getServiceName());
                List list=new ArrayList();
                map.put("type","1");
                map.put("serviceName",serviceNameDto.getServiceName());
                //查服务下的实例
                List<PaasOutageDTO> containerlist=slaOutageService.getPaasOutageList(map);
                if(containerlist.size()>0){
                    for (int j = 0; j < containerlist.size(); j++) {
                        PaasOutageDTO containerDto=containerlist.get(j);
                        map.put("type","2");
                        map.put("containerId",containerDto.getContainerId());
                        //查实例的宕机时间
                        List<PaasOutageDTO> timelist=slaOutageService.getPaasOutageList(map);
                        list.add(timelist);
                    }
                }
                //遍历list，取时间交集
                if(list.size()>0){
                    if(list.size()==1){
                        //只有一个实例，取并集
                        List<PaasOutageDTO> timelist= (List<PaasOutageDTO>) list.get(0);
                        for (int j = 0; j <timelist.size() ; j++) {
                            PaasOutageDTO timeDto=timelist.get(j);
                            insertSlaOutage(dto, timeDto);
                        }
                    }else{
                        //多个实例，取交集
                        List<PaasOutageDTO> timelist0=(List<PaasOutageDTO>) list.get(0);
                        for (int j = 1; j < list.size(); j++) {
                            //定义一个空list
                            List<PaasOutageDTO> nulllist=new ArrayList<PaasOutageDTO>();
                            //取出一个与其他实例的时间做比较
                            for (int k = 0; k <timelist0.size() ; k++) {
                                PaasOutageDTO timeDto=timelist0.get(k);
                                Timestamp downtimeBegin=timeDto.getDowntimeBegin();
                                Timestamp downtimeEnd=timeDto.getDowntimeEnd();
                                long start_time=downtimeBegin.getTime();
                                long end_time=downtimeEnd.getTime();
                                //取出其他实例的时间集合
                                List<PaasOutageDTO> timelist=(List<PaasOutageDTO>) list.get(j);
                                for (int l = 0; l <timelist.size() ; l++) {
                                    //取交集
                                    PaasOutageDTO timeDto2=timelist.get(l);
                                    long S_time=timeDto2.getDowntimeBegin().getTime();
                                    long S_end=timeDto2.getDowntimeEnd().getTime();
                                    long a_stime=start_time;
                                    long a_etime=end_time;
                                    if(S_time>=a_stime){
                                        a_stime=S_time;
                                    }
                                    if(S_end<=a_etime){
                                        a_etime=S_end;
                                    }
                                    if(a_stime<a_etime){
                                        PaasOutageDTO nulldto=new PaasOutageDTO();
                                        nulldto.setDowntimeBegin(new Timestamp(a_stime));
                                        nulldto.setDowntimeEnd(new Timestamp(a_etime));
                                        nulllist.add(nulldto);
                                    }
                                }
                            }
                            if(nulllist.size()==0){
                                break;
                            }else{
                                timelist0=nulllist;
                            }
                            if(j==list.size()-1){
                                //执行插入
                                for (int k = 0; k < timelist0.size(); k++) {
                                    PaasOutageDTO pdto=timelist0.get(k);
                                    insertSlaOutage(dto,pdto);
                                }
                            }
                        }
                    }
                }
            }
        }
        logger.info("每天早上6点执行一次从paas数据库取宕机信息。结束……");
        //发邮件
        logger.info("每天早上6点系统宕机发邮件--开始");
        Map mm=new HashMap();
        Date dates=new Date();
        String fromDate=sdf.format(dates)+" 00:00:00";
        String toDate=sdf.format(date)+" 23:59:59";
        mm.put("fromDate", fromDate);
        mm.put("toDate", toDate);
        List<SlaProjectUserInfoDTO> slaProjectUserInfoDTOlist=slaOutageService.getPMList(map);
        if(slaProjectUserInfoDTOlist.size()>0){
            for (int i = 0; i <slaProjectUserInfoDTOlist.size() ; i++) {
                SlaProjectUserInfoDTO dto=slaProjectUserInfoDTOlist.get(i);
                String mail=dto.getEmail();
                Map mmm=new HashMap();
                mmm.put("userId",dto.getUserId());
                sendEmail(time+"有"+slaOutageService.getServiceNum(mmm).size()+"条宕机信息需要您确认","系统宕机确认",mail);
            }
        }
        logger.info("每天早上6点系统宕机发邮件--结束");
    }

    /**
     * 执行插入宕机表
     * @param dto
     * @param timeDto
     */
    public void insertSlaOutage(SlaOutageDTO dto, PaasOutageDTO timeDto) {
        dto.setOutageStartDate(timeDto.getDowntimeBegin());
        dto.setOutageEndDate(timeDto.getDowntimeEnd());
        //执行插入
        dto.setCreateTime(new Timestamp(new Date().getTime()));
        dto.setStatus("0");
        dto.setIfDel("0");
        long num = (timeDto.getDowntimeEnd().getTime() -timeDto.getDowntimeBegin().getTime()) / 1000 / 60;
        dto.setOutageTime((int) num);
        if (num > 30) {
            dto.setIfOvertime("1");
        }else{
            dto.setIfOvertime("0");
        }
        if (num >= 30) {
            dto.setDeductScore(10);
            dto.setCutPayment(2);
        }else{
            dto.setDeductScore(0);
            dto.setCutPayment(0);
        }
        dto.setIfNotRun("0");
        dto.setAssessStatus("1");
        //查alm s码  应用简称
        Map mm=new HashMap();
        mm.put("projectName",dto.getProjectName());
        List<SlaProjectInfoDTO> slaProjectInfoDTOList=slaOutageService.getSlaProjectInfoList(mm);
        if(slaProjectInfoDTOList.size()>0){
            SlaProjectInfoDTO slaProjectInfoDTO=slaProjectInfoDTOList.get(0);
            dto.setAlmApplicationId(slaProjectInfoDTO.getAlmApplicationId());
            dto.setAlmShortName(slaProjectInfoDTO.getAlmShortName());
        }
        slaOutageService.insertSlaOutagelData(dto);
    }
    /**
     *
     * 每天定时更新项目信息
     */
   /* @Scheduled(fixedRate=120000)*/
    /*@Scheduled(cron="${com.project.time}") */ //每天早上2点执行一次
    public void statusCheck2() {
        logger.info("每天早上2点执行一次获取项目信息。开始……");
        //先删后插
        List<ProjectInfoData> projectInfoDatalist=null;
        try{
            projectInfoDatalist = profilerProjectService.getProjectInfoFromPaaS();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(projectInfoDatalist.size()>0){
            String str="";
            //slaProjectInfoService.delProjectInfo();
            List<String> listnames = new ArrayList<String>();
            for (int i = 0; i <projectInfoDatalist.size() ; i++) {
                ProjectInfoData projectInfoData=projectInfoDatalist.get(i);
                listnames.add(projectInfoData.getProjectName());
                Map map=new HashMap();
                map.put("projectName",projectInfoData.getProjectName());
                List<SlaProjectInfoDTO> list=slaProjectInfoService.getSlaProjectInfoList(map);
                SlaProjectInfoDTO dto=new SlaProjectInfoDTO();
                int id=0;
                if(list!=null&&list.size()>0){
                    //更新
                    dto=list.get(0);
                    dto.setProjectId(projectInfoData.getProjectId()+"");
                    dto.setAlmShortName(projectInfoData.getAlmShortName());
                    dto.setAlmApplicationId(projectInfoData.getAlmApplicationId());
                    slaProjectInfoService.updateSlaProjectInfo(dto);
                    //项目经理信息表先删后增
                    slaProjectInfoService.delProjectUserInfo(dto.getId());
                }else{
                    //新增
                    dto.setAlmApplicationId(projectInfoData.getAlmApplicationId());
                    dto.setAlmShortName(projectInfoData.getAlmShortName());
                    dto.setProjectId(projectInfoData.getProjectId()+"");
                    dto.setProjectName(projectInfoData.getProjectName());
                    dto=slaProjectInfoService.insertSlaProjectInfo(dto);
                }
                id=dto.getId();
                insertUserInfo(projectInfoData, id);
            }
            //删除不在列表里的项目
            Map mmmm=new HashMap();
            mmmm.put("list",listnames);
            slaProjectInfoService.delProjectInfo(mmmm);
        }else{
            logger.info("paas未有项目信息数据返回");
        }
        logger.info("每天早上2点执行一次获取项目信息。结束。");
    }

    /**
     * 插入paas项目项目成员信息
     * @param projectInfoData
     * @param id
     */
    public void insertUserInfo(ProjectInfoData projectInfoData, int id) {
        List<ProjectUserInfo> userlist=projectInfoData.getUserList();
        if(userlist.size()>0){
            for (int j = 0; j < userlist.size(); j++) {
                ProjectUserInfo userInfo=userlist.get(j);
                SlaProjectUserInfoDTO userDto=new SlaProjectUserInfoDTO();
                userDto.setAccount(userInfo.getAccount());
                userDto.setEmail(userInfo.getEmail());
                userDto.setRole(userInfo.getRole());
                userDto.setTSlaProjectInfoId(id);
                userDto.setUserId(userInfo.getUserId());
                userDto.setUserName(userInfo.getUserName());
                slaProjectInfoService.insertSlaProjectUserInfo(userDto);
            }
        }
    }

    /**
     *每天定时取dubbo异常数据
     */
    @Scheduled(cron="${com.monitor.time}")  //每天早上6点执行一次
    public void statusCheck3() {
        logger.info("每天早上6点执行一次获取dubbo异常数据。开始……");
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
                    List<AppProfilerData> appProlist=null;
                    try{
                        appProlist = profilerReadService.getProfilersByDateAndProjectName(start_date,end_date,projectName);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
                    }else{
                        logger.info("paas未有dubbo异常数据返回");
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        logger.info("每天早上6点执行一次获取dubbo异常数据。结束。");
    }
    /**
     *每2分钟更新alm-jira关联关系
     *  */
    @Scheduled(fixedRate=120000)
    public void statusCheck4() {
        logger.info("每2分钟执行更新alm-jira关联关系----开始执行");
        //查未更新的jira关联关系
        Map map=new HashMap();
        map.put("jiraUpdateStatus","0");
        List<SlaALMJIRARelationHistoryDTO> relationHistoryList=slaALMJIRARelationHistoryService.getRelationList(map);
        updateRelation(relationHistoryList,1);

        //查未更新的alm关联关系
        map.put("jiraUpdateStatus",null);
        map.put("almUpdateStatus","0");
        relationHistoryList=slaALMJIRARelationHistoryService.getRelationList(map);
        updateRelation(relationHistoryList,2);
        logger.info("每2分钟执行更新alm-jira关联关系----结束执行");
    }

    /**
     * 执行更新关联关系
     * @param relationHistoryList
     * @param type
     */
    public void updateRelation(List<SlaALMJIRARelationHistoryDTO> relationHistoryList,int type) {
        if(relationHistoryList.size()>0){
            for (int i = 0; i < relationHistoryList.size(); i++) {
                SlaALMJIRARelationHistoryDTO dto=relationHistoryList.get(i);
                //更新状态
                if(type==1){
                    //调jira jar包接口


                    dto.setJiraUpdateStatus("1");
                    dto.setJiraUpdateTime(new Timestamp(new Date().getTime()));
                }else{
                    //调alm接口


                    dto.setAlmUpdateStatus("2");
                    dto.setAlmUpdateTime(new Timestamp(new Date().getTime()));
                }
                slaALMJIRARelationHistoryService.updateJiraRelation(dto);
                //插log表
                SlaALMJIRARelationLogDTO slaLogDTO=new SlaALMJIRARelationLogDTO();
                slaLogDTO.setIfDel("0");
                slaLogDTO.setTSlaAlmJiraRelationHistoryId(dto.getId());
                if(type==1){
                    slaLogDTO.setType("1");
                }else{
                    slaLogDTO.setType("2");
                }
                slaLogDTO.setUpdateStatus("1");
                slaLogDTO.setUpdateTime(new Timestamp(new Date().getTime()));
                slaALMJIRARelationHistoryService.insertLog(slaLogDTO);
            }
        }
    }
    @Value("${spring.mail.username}")
    private String from;
    /**
     * 发邮件
     * @return
     */
    public Object sendEmail(String content,String title,String mail)
    {
        try
        {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(from);
            message.setTo(mail);
            message.setSubject(title);
            message.setText(content);
            this.mailSender.send(mimeMessage);
            return null;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    /**
     *每周五12点对本周内dubbo异常调用比例超1%的发邮件
     */
    @Scheduled(cron="${com.monitor.mail.time}")  //
    public void statusCheck5() {
        logger.info("每周五12点对本周内dubbo异常调用比例超1%的发邮件----开始执行");
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date(new Date().getTime());
        String fromDate = matter1.format(date);
        date=new Date(new Date().getTime()-7*24*60*60*1000);
        String toDate= matter1.format(date);;
        Map map = new HashMap();
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        SlaMonitorInterfaceDTO slaMonitorInterfaceDTO = new SlaMonitorInterfaceDTO();
        SlaMonitorDTO slaMonitorDTO = slaMonitorService.getPercent(map);
        slaMonitorInterfaceDTO.setInvokedTotal(new Double(slaMonitorDTO.getInvokedTotal()).intValue());//总次数
        slaMonitorInterfaceDTO.setInvokedSuccessTotal(new Double(slaMonitorDTO.getInvokedSuccessTotal()).intValue());//成功次数
        slaMonitorInterfaceDTO.setInvokedFailTotal(new Double(slaMonitorDTO.getInvokedFailTotal()).intValue());//失败次数
        double percent = slaMonitorDTO.getInvokedFailTotal() / slaMonitorDTO.getInvokedTotal();
        slaMonitorInterfaceDTO.setPercent(percent * 100 + "%");//稳定性
        if (percent >= 0.01) {
            List<SlaProjectUserInfoDTO> slaProjectUserInfoDTOlist=slaMonitorService.getPMMonitorList(map);
            if(slaProjectUserInfoDTOlist.size()>0){
                for (int i = 0; i <slaProjectUserInfoDTOlist.size() ; i++) {
                    SlaProjectUserInfoDTO dto=slaProjectUserInfoDTOlist.get(i);
                    String mail=dto.getEmail();
                    Map mmm=new HashMap();
                    mmm.put("userId",dto.getUserId());
                    sendEmail(fromDate+"至"+toDate+"有"+slaMonitorService.getServiceNum(mmm).size()+"条dubbo异常信息需要您确认","dubbo异常确认",mail);
                }
            }
        }
        logger.info("每周五12点对本周内dubbo异常调用比例超1%的发邮件----结束执行");
    }
}
