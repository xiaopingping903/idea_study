package com.haier.adp.sla.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haier.adp.common.constants.Constants;
import com.haier.adp.jira.MetricJiraService;
import com.haier.adp.jira.dto.BugStatisticsInfo;
import com.haier.adp.jira.impl.MetricJiraServiceImpl;
import com.haier.adp.sla.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rsdeep on 2017/1/13.
 */
@Slf4j
@WebService
public class SlaServiceImpl implements SlaService{
    @Autowired
    private SlaOTDService slaOTDService;
    @Autowired
    private SlaOutageService slaOutageService;
    @Autowired
    private SlaBugInfoService slaBugInfoService;
    @Autowired
    private SlaProjectRelService slaProjectRelService;
    @Autowired
    private SlaMonitorService slaMonitorService;
    @Autowired
    private static final MetricJiraService jiraService = new MetricJiraServiceImpl();
    @Autowired
    private SlaInterfaceService slaInterfaceService;
    @Autowired
    private SlaProjectInfoService slaProjectInfoService;
    @Autowired
    private SlaBonuseService slaBonuseService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public AlmResult querySlaData(Object para) {
        AlmResult resultDTO = new AlmResult();
        JSON json = (JSON)JSON.toJSON(para);
        AlmListDataDTO listData = JSON.toJavaObject(json, AlmListDataDTO.class);
        //task list
        List<SlaDetailDTO> detailList = new ArrayList<SlaDetailDTO>();
        if (listData.getTaskIds() != null && !"".equals(listData.getTaskIds())){
            String[] taskString = listData.getTaskIds().split(",");
            for (String task : taskString) {
                List<SlaDetailDTO> detailResult = new ArrayList<SlaDetailDTO>();
                if (taskString != null && !"".equals(taskString)) {
                    SlaDetailDTO dto = new SlaDetailDTO();
                    dto.setAlmTaskId(task);
                    detailResult = slaOTDService.getSlaDetail(dto);
                    detailList.addAll(detailResult);
                }
            }
            resultDTO.setTaskList(detailList);
        }
        //bug list
        List<BugInfoDTO> bugResult = new ArrayList<BugInfoDTO>();
        bugResult = getBugInfoList(listData);
        resultDTO.setBugList(bugResult);
        //宕机信息
        String projectStr = getAlmShortNameByScode(listData.getAlmApplicationSCodes());
        List<AlmOutageInterfaceDTO> outageAlmResult = new ArrayList<AlmOutageInterfaceDTO>();
        SlaOutageInterfaceDTO outageDTO = new SlaOutageInterfaceDTO();
        if(projectStr != null && !"".equals(projectStr)){
            String[] projects = projectStr.split(",");
            for (String projectName : projects) {
                String queryStartStr = listData.getStageStartDate() + " 00:00:00";
                String queryEndStr = listData.getStageEndDate() + " 23:59:59";
                Map map = new HashMap();
                map.put("projectName", projectName);
                List<SlaProjectInfoDTO> projectInfo = slaProjectInfoService.getSlaProjectInfoList(map);
                if(projectInfo != null && projectInfo.size() > 0){
                    String sCode = projectInfo.get(0).getAlmApplicationId();
                    String supplierId = listData.getSupplierId();
                    outageDTO = slaInterfaceService.getOutageInfo(sCode, queryStartStr, queryEndStr, supplierId);
                    AlmOutageInterfaceDTO outDTO = new AlmOutageInterfaceDTO();
                    try {
                        BeanUtils.copyProperties(outDTO, outageDTO);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    outageAlmResult.add(outDTO);
                }
            }
        }
        resultDTO.setOutageList(outageAlmResult);
        //dubbo信息
        List<SlaMonitorInterfaceDTO> dubboResult = new ArrayList<SlaMonitorInterfaceDTO>();
        SlaMonitorInterfaceDTO dubboDTO = new SlaMonitorInterfaceDTO();
        if(projectStr != null && !"".equals(projectStr)){
            String[] projects = projectStr.split(",");
            for (String projectName : projects) {
                Map map = new HashMap();
                map.put("projectName", projectName);
                List<SlaProjectInfoDTO> projectInfo = slaProjectInfoService.getSlaProjectInfoList(map);
                String queryStartStr = listData.getStageStartDate();
                String queryEndStr = listData.getStageEndDate();
                if(projectInfo != null && projectInfo.size() > 0){
                    String sCode = projectInfo.get(0).getAlmApplicationId();
                    String supplierId = listData.getSupplierId();
                    dubboDTO = slaInterfaceService.getMointorInfo(sCode, queryStartStr, queryEndStr, supplierId);
                    dubboResult.add(dubboDTO);
                }
            }
        }
        resultDTO.setDubboList(dubboResult);
        //SLA奖励信息
        List<SlaBonuseInterfaceDTO> bonusResult = new ArrayList<SlaBonuseInterfaceDTO>();
        SlaBonuseInterfaceDTO bonusDTO = new SlaBonuseInterfaceDTO();
        if(projectStr != null && !"".equals(projectStr)){
            String[] projects = projectStr.split(",");
            for (String projectName : projects) {
                Map map = new HashMap();
                map.put("projectName", projectName);
                List<SlaProjectInfoDTO> projectInfo = slaProjectInfoService.getSlaProjectInfoList(map);
                String queryStartStr = listData.getStageStartDate();
                String queryEndStr = listData.getStageEndDate();
                if(projectInfo != null && projectInfo.size() > 0){
                    String sCode = projectInfo.get(0).getAlmApplicationId();
                    String supplierId = listData.getSupplierId();
                    bonusDTO = slaInterfaceService.getBonuseInfo(sCode, queryStartStr, queryEndStr, supplierId);
                    bonusResult.add(bonusDTO);
                }
            }
        }
        resultDTO.setBonuseList(bonusResult);
        return resultDTO;
    }

    @Override
    public AlmReturnValue insertResourcePoolListData(Object para) {
        AlmReturnValue returnValue = new AlmReturnValue();
        JSON json = (JSON)JSON.toJSON(para);
        AlmListDataDTO insertListData = JSON.toJavaObject(json, AlmListDataDTO.class);
        String type = Constants.PROJECT_TYPE.RESOURCE_POOL_PROJECT;
        //存储一条记录到一览表，同时更新detai表中对应story的关联字段
        int listId = insertListData(insertListData, type);
        if(listId > 0){
            //维护 SLA-PROJECT 关系表，并根据项目名字和时间关联宕机表,dubbo表
            String[] almSCodes = insertListData.getAlmApplicationSCodes().split(",");
            for (String sCode : almSCodes) {
                Map searchMap = new HashMap();
                searchMap.put("almApplicationId", sCode);
                List<SlaProjectInfoDTO> projectInfoList = slaProjectInfoService.getSlaProjectInfoList(searchMap);
                if(projectInfoList != null && projectInfoList.size() > 0){
                    String project = projectInfoList.get(0).getAlmShortName();
                    SlaProjectRelDTO dto = new SlaProjectRelDTO();
                    dto.setListId(listId);
                    dto.setProjectName(project);
                    dto.setAlmAppScode(sCode);
                    slaProjectRelService.insertSlaProjectRel(dto);
                    //outage
                    Map outageMap = new HashMap();
                    outageMap.put("listId", listId);
                    outageMap.put("startDate", insertListData.getStageStartDate());
                    outageMap.put("endDate", insertListData.getStageEndDate());
                    outageMap.put("projectName", project);
                    slaOutageService.updateSlaListId(outageMap);
                    //dubbo
                    Map dubboMap = new HashMap();
                    dubboMap.put("listId", listId);
                    dubboMap.put("startDate", insertListData.getStageStartDate());
                    dubboMap.put("endDate", insertListData.getStageEndDate());
                    dubboMap.put("projectName", project);
                    slaMonitorService.updateSlaListId(dubboMap);
                    //bonus
                    Map bonusMap = new HashMap();
                    bonusMap.put("listId", listId);
                    bonusMap.put("startDate", insertListData.getStageStartDate());
                    bonusMap.put("endDate", insertListData.getStageEndDate());
                    bonusMap.put("projectName", project);
                    slaBonuseService.updateSlaListId(bonusMap);
                }
                //bug-sla插入
                insertBugInfo(insertListData, listId);
            }
        } else {
            log.error("SLA记录失败，请确认数据格式。{}", para.toString());
        }
        return returnValue;
    }

    @Override
    public AlmReturnValue insertResourcePoolDetailData(Object para) {
        AlmReturnValue returnValue = new AlmReturnValue();
        JSON json = (JSON)JSON.toJSON(para);
        AlmDetailDataDTO detailPara = JSON.toJavaObject(json, AlmDetailDataDTO.class);
        String type = Constants.PROJECT_TYPE.RESOURCE_POOL_PROJECT;
        returnValue = insertDetailData(detailPara, type);
        return returnValue;
    }
    private List<BugInfoDTO> getBugInfoList(AlmListDataDTO dto){
        List<BugInfoDTO> result = new ArrayList<BugInfoDTO>();
        String project = getAlmShortNameByScode(dto.getAlmApplicationSCodes());
        if(project != null && !"".equals(project)){
            String[] projects = project.split(",");
            for (String projectName : projects) {
                Date startDate;
                Date endDate;
                try {
                    startDate = sdf.parse(dto.getStageStartDate());
                    endDate = sdf.parse(dto.getStageEndDate());
                    List<BugStatisticsInfo> resultBug = jiraService.getBugStatistics(projectName, startDate, endDate);
                    for (BugStatisticsInfo info : resultBug) {
                        BugInfoDTO bugDto = new BugInfoDTO();
                        bugDto.setProjectName(projectName);
                        bugDto.setQuantity(info.getQuantity());
                        bugDto.setLink(info.getLink());
                        bugDto.setType(info.getLevel());
                        String level = info.getLevel();
                        int cutPoints = 0;
                        double cutPayment = 0;
                        switch(level){
                            case "blocker":
                                cutPoints = 20 * info.getQuantity();
                                cutPayment = 1;
                                break;
                            case "critical":
                                cutPoints = 10 * info.getQuantity();
                                cutPayment = 0.5;
                                break;
                            case "major":
                                cutPoints = 5 * info.getQuantity();
                                cutPayment = 0.3;
                                break;
                        }
                        bugDto.setCutPoints(cutPoints);
                        bugDto.setCutPayment(cutPayment);
                        result.add(bugDto);
                    }
                } catch (Exception e) {
                    log.error("日期格式错误。{}", e);
                }
            }
        }
        return result;
    }
    private String getAlmShortNameByScode(String sCodeList){
        StringBuffer projectStrBuffer = new StringBuffer();
        String[] almSCodes = sCodeList.split(",");
        for (String sCode : almSCodes) {
            Map searchMap = new HashMap();
            searchMap.put("almApplicationId", sCode);
            List<SlaProjectInfoDTO> projectInfoList = slaProjectInfoService.getSlaProjectInfoList(searchMap);
            if (projectInfoList != null && projectInfoList.size() > 0){
                projectStrBuffer.append(projectInfoList.get(0).getAlmShortName() + ",");
            }
        }
        String projectStr = projectStrBuffer.toString();
        if(!"".equals(projectStr)){
            projectStr = projectStr.substring(0, projectStr.length() - 1);
        }
        return projectStr;
    };
    private void insertBugInfo(AlmListDataDTO insertListData, int listId) {
        List<BugInfoDTO> result = new ArrayList<BugInfoDTO>();
        result = getBugInfoList(insertListData);
        for (BugInfoDTO bugDTO : result) {
            int insertCount = slaBugInfoService.insertBugInfo(bugDTO);
            if (insertCount == 0){
                log.error("插入BUG信息出错。{}" ,bugDTO.toString());
            }
        }
    }
    private int insertListData(AlmListDataDTO dto, String type){
        String[] taskIds = dto.getTaskIds().split(",");
        SlaListDTO insertDTO = new SlaListDTO();
        try {
            BeanUtils.copyProperties(insertDTO, dto);
        } catch (IllegalAccessException e) {
            log.error("ALM入参错误。{}", e);
        } catch (InvocationTargetException e) {
            log.error("复制数据到List发生错误。{}", e);
        }
        insertDTO.setType(type);
        insertDTO.setIfPaid(true);
        insertDTO.setIfShown(true);
        int listId = slaOTDService.insertListData(insertDTO);
        for (int i = 0; i < taskIds.length; i++) {
            SlaDetailDTO updateDTO = new SlaDetailDTO();
            updateDTO.setAlmTaskId(taskIds[i]);
            updateDTO.setListId(listId);
            slaOTDService.updateListIdForDetailData(updateDTO);
        }
        return listId;
    }
    public AlmReturnValue insertDetailData(AlmDetailDataDTO insertList, String type){
        AlmReturnValue returnValue = new AlmReturnValue();
        String[] taskIds = insertList.getAlmTaskId().split(",");
        for (String taskId : taskIds) {
            //try {
            //Issue storyData = basicService.getJiraIssue(taskIds);
            SlaDetailDTO insertDTO = new SlaDetailDTO();
            insertDTO.setProjectName("adp-test");
            insertDTO.setAlmTaskId(taskId);
            insertDTO.setDelayDays(2);
            insertDTO.setType("res");
            insertDTO.setIfOnTime(false);
            insertDTO.setAlmAppId("as001");
            insertDTO.setAssignPerson("zwd");
            insertDTO.setIfPaid(true);
            insertDTO.setJiraEpicId("J001");
            insertDTO.setIfSatisfied(false);
            insertDTO.setJiraStoryId("j001");
            insertDTO.setAlmRequestId("A001");
            insertDTO.setRequestPlanDate("2016-12-12");
            insertDTO.setRequestActualDate("2016-12-12");
            insertDTO.setRequestPlanDate("2016-12-12");
            insertDTO.setAlmTaskId("A001");
            int detailId = slaOTDService.insertDetailData(insertDTO);
            System.out.print(detailId);
//            } catch (URISyntaxException e) {
//                log.error("查询JIRA信息时出错.{}", e);
//            }
        }

//            SlaDetailDTO detailDTO = new SlaDetailDTO();
//            detailDTO.setJiraEpicId(insertDTO.getJiraEpicId());
//            detailDTO.setJiraStoryId(insertDTO.getJiraStoryId());
//            detailDTO.setAlmRequestId(insertDTO.getAlmRequestId());
//            detailDTO.setAlmTaskId(insertDTO.getAlmTaskId());
//            detailDTO.setIfOnTime(insertDTO.isIfOnTime());
//            detailDTO.setRequestPlanDate(insertDTO.getRequestPlanDate());
//            detailDTO.setRequestActualDate(insertDTO.getRequestActualDate());
//            detailDTO.setApplyPD(insertDTO.getApplyPD());
//            SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
//            Date planDate = new Date();
//            Date actDate = new Date();
//            try {
//                planDate = smp.parse(insertDTO.getRequestPlanDate());
//                actDate = smp.parse(insertDTO.getRequestActualDate());
//            } catch (ParseException e) {
//                log.error("日期格式错误。{}", e);
//            }
//
//            int delayDays = daysBetween(actDate, planDate);
//            detailDTO.setDelayDays(delayDays);
//
//            detailDTO.setAssignPerson(insertDTO.getAssignPerson());
//            int lostPoints = Constants.POINT_RULE.LOST_POINTS_PER_DAY * delayDays;
//            detailDTO.setCutPoints(lostPoints);
//            double managerPrice = insertDTO.getManagerPrice() * Constants.MONEY_RULE.DETAIL_LOST_MONEY_PARA;
//            detailDTO.setCutPayment((int) managerPrice);
//            detailDTO.setType(type);
//            detailDTO.setSpentDays(insertDTO.getSpentDays());
//            detailDTO.setProjectName(insertDTO.getProjectName());
////            detailDTO.setIfPaid();
//            int detailId = slaOTDService.insertDetailData(detailDTO);
        return returnValue;
    }
}
