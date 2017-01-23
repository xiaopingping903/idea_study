package com.haier.adp.sla.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haier.adp.common.constants.Constants;
import com.haier.adp.jira.BasicService;
import com.haier.adp.jira.MetricJiraService;
import com.haier.adp.jira.dto.BugStatisticsInfo;
import com.haier.adp.jira.dto.StoryTransferInfo;
import com.haier.adp.jira.impl.MetricJiraServiceImpl;
import com.haier.adp.sla.dto.*;
import com.haier.adp.sla.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Controller
@Slf4j
@RequestMapping("/adp/sla")
public class SlaController {

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
    private static final BasicService basicService = new BasicService();
    @Autowired
    private SlaInterfaceService slaInterfaceService;
    @Autowired
    private SlaProjectInfoService slaProjectInfoService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @RequestMapping(value = "/getSlaList", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JSONObject getSlaList(@RequestBody Map para) {
        SlaListDTO searchDTO = new SlaListDTO();
        int pageNo = 0;
        int pageSize = 10;
        if (para.get("fromDate") != null && para.get("toDate") != null){
            try {
                String dateStringFrom = para.get("fromDate").toString();
                searchDTO.setQueryStartDate(sdf.parse(dateStringFrom));
                String dateStringEnd = para.get("toDate").toString();
                searchDTO.setQueryEndDate(sdf.parse(dateStringEnd));
            } catch (ParseException e) {
                log.error("日期格式错误。{}+++{}", para.get("fromDate").toString(), para.get("toDate").toString());
            }
        }
        if(para.get("projectName") != null){
            searchDTO.setProjectName(para.get("projectName").toString());
        }
        if(para.get("contractStageDesc") != null){
            searchDTO.setContractStageDesc(para.get("contractStageDesc").toString());
        }
        if(para.get("pageNo") != null && para.get("pageSize") != null){
            pageNo = Integer.parseInt(para.get("pageNo").toString());
            pageSize = Integer.parseInt(para.get("pageSize").toString());
        }
        if(pageNo >= 1){
            pageNo = pageNo - 1;
        }
        int startNo = pageNo * pageSize;
        searchDTO.setStartNo(startNo);
        searchDTO.setPageSize(pageSize);
        int count = slaOTDService.queryListCount(searchDTO);
        List<SlaListDTO> result = slaOTDService.getSlaList(searchDTO);

        JSONObject resultData = new JSONObject();
        resultData.put("total", count);
        resultData.put("list", result);
        return resultData;
    }
    @RequestMapping(value = "/getSysOperEntranceData", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JSONObject getSysOperEntranceData(@RequestBody Map para) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(para);
        JSONObject resultData = new JSONObject();
        Date fromDate = new Date();
        Date toDate =  new Date();
        try {
            if(jsonObject.get("fromDate") != null){
                fromDate = sdf.parse(jsonObject.get("fromDate").toString());
            }
            if(jsonObject.get("toDate") != null){
                toDate = sdf.parse(jsonObject.get("toDate").toString());
            }
        } catch (ParseException e) {
            log.error("查询日期格式错误。{}, {}", fromDate, toDate);
            return resultData;
        }
        String projectName = "";
        if (jsonObject.get("projectName") != null){
            projectName = jsonObject.get("projectName").toString();
        } else{
            log.error("查询项目名称为空.");
            return resultData;
        }
        // story
        List<StoryTransferInfo> storyList = new ArrayList<StoryTransferInfo>();
        int storyCount = 0;
        try {
            // TODO  jar包查询错误 LINE191
            storyList = jiraService.getStoryList(projectName, fromDate, toDate);
            storyCount = storyList.size();
        } catch (Exception e) {
            log.error("查询时出错.{}", e);
        }
        JSONObject storyResult = new JSONObject();
        storyResult.put("total", storyCount);
        storyResult.put("list", storyList);
        resultData.put("storyList", storyResult);
        // bug
        List<BugStatisticsInfo> bugList = new ArrayList<BugStatisticsInfo>();
        int bugCount = 0;
        try {
            bugList = jiraService.getBugStatistics(projectName, fromDate, toDate);
            bugCount = bugList.size();
        } catch (Exception ex) {
            log.error("查询JIRA信息时发生系统错误。{}", ex);
        }

        JSONObject bugResult = new JSONObject();
        bugResult.put("total", bugCount);
        bugResult.put("list", bugList);
        resultData.put("bugList", bugResult);
        List<SlaOutageDTO> outageList = new ArrayList<SlaOutageDTO>();
        int outageCount = 0;
        //outage
        Map queryPara = new HashMap();
        queryPara.put("projectName", projectName);
        queryPara.put("fromDate", fromDate);
        queryPara.put("toDate", toDate);
        outageList = slaOutageService.getSlaOutageList(queryPara);
        outageCount = slaOutageService.querySlaOutageListCount(queryPara);
        JSONObject outageResult = new JSONObject();
        outageResult.put("total", outageCount);
        outageResult.put("list", outageList);
        resultData.put("outageList", outageResult);

        return resultData;
    }

    @RequestMapping(value = "/getSlaDetail", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JSONObject getSlaDetail(@RequestBody Map para) {
        JSONObject resultData = new JSONObject();
        // sla1
        SlaDetailDTO slaDetailDTO = new SlaDetailDTO();
        List<SlaDetailDTO> taskList = new ArrayList<SlaDetailDTO>();
        int taskCount = 0;
        if(para.get("listId") != null){
            slaDetailDTO.setListId(Integer.parseInt(para.get("listId").toString()));
            taskList = slaOTDService.getSlaDetail(slaDetailDTO);
            taskCount = slaOTDService.queryDetailCount(slaDetailDTO);
        }
        SlaListDTO search = new SlaListDTO();
        search.setId(Integer.parseInt(para.get("listId").toString()));
        search.setStartNo(0);
        search.setPageSize(100);
        List<SlaListDTO> searchList = slaOTDService.getSlaList(search);
        String queryStartDate = "";
        String queryEndDate = "";
        if (searchList != null){
            queryStartDate = searchList.get(0).getStageBeginDate().toString();
            queryEndDate = searchList.get(0).getStageEndDate().toString();
        }
        JSONObject taskResult = new JSONObject();
        taskResult.put("total", taskCount);
        taskResult.put("list", taskList);
        resultData.put("taskList", taskResult);
        //sla 2
        JSONObject sla2Object = new JSONObject();
        if (para.get("listId") != null){
            SlaProjectRelDTO rel = new SlaProjectRelDTO();
            rel.setListId(Integer.parseInt(para.get("listId").toString()));
            List<SlaProjectRelDTO> projectNames = slaProjectRelService.getRelList(rel);
            if (projectNames != null && projectNames.size() > 0){
                sla2Object.put("sla2Total", projectNames.size());
                List sla2List = new ArrayList();
                for (SlaProjectRelDTO dto : projectNames) {
                    JSONObject result = new JSONObject();
                    String projectName = dto.getProjectName();
                    result.put("projectName", projectName);
                    // sla2 bug
                    List<BugInfoDTO> bugInfoList = new ArrayList<BugInfoDTO>();
                    int bugCount = 0;
                    BugInfoDTO searchDTO = new BugInfoDTO();
                    searchDTO.setListId(Integer.parseInt(para.get("listId").toString()));
                    searchDTO.setProjectName(projectName);
                    bugInfoList = slaBugInfoService.getBugInfoList(searchDTO);
                    bugCount = slaBugInfoService.querySlaBugInfoCount(searchDTO);
                    JSONObject bugInfoResult = new JSONObject();
                    bugInfoResult.put("total", bugCount);
                    bugInfoResult.put("list", bugInfoList);
                    result.put("bugList", bugInfoResult);
                    // sla2 outage
                    SlaOutageInterfaceDTO outageDTO = new SlaOutageInterfaceDTO();
                    int outageCount = 0;
                    if(para.get("listId") != null){
                        Map queryPara = new HashMap();
                        queryPara.put("tSlaListId", para.get("listId").toString());
                        int listId = Integer.parseInt(para.get("listId").toString());
                        queryPara.put("almShortName", projectName);
                        outageDTO = slaOutageService.getOutageInfo(projectName, listId, queryStartDate, queryEndDate);
                        outageCount = slaOutageService.querySlaOutageListCount(queryPara);
                    }
                    JSONObject outageResult = new JSONObject();
                    outageResult.put("total", outageCount);
                    outageResult.put("list", outageDTO.getList());
                    outageResult.put("annualDeductScore", outageDTO.getAnnualDeductScore());
                    outageResult.put("annualCutPayment", outageDTO.getAnnualCutPayment());
                    result.put("outageList", outageResult);
                    // sla2 dubbo
                    List<SlaMonitorInterfaceDTO> dubboList = new ArrayList<SlaMonitorInterfaceDTO>();
                    SlaMonitorInterfaceDTO dubboDto = new SlaMonitorInterfaceDTO();
                    int dubboCount = 0;
                    if(para.get("listId") != null){
                        Map queryPara = new HashMap();
                        queryPara.put("listId", para.get("listId").toString());
                        int listId = Integer.parseInt(para.get("listId").toString());
                        queryPara.put("almShortName", projectName);
                        dubboDto = slaMonitorService.getMointorInfo(projectName, listId);
                        outageCount = slaMonitorService.querySlaMonitorListCount(queryPara);
                        dubboList.add(dubboDto);
                    }
                    JSONObject dubboResult = new JSONObject();
                    dubboResult.put("total", outageCount);
                    dubboResult.put("list", dubboList);
                    result.put("dubboList", dubboResult);
                    //sla2 bonus
                    JSONObject bonusResult = new JSONObject();
                    List bonusList = new ArrayList();
                    bonusResult.put("total", 0);
                    bonusResult.put("list", bonusList);
                    result.put("bonusList", bonusResult);
                    if(result != null){
                        sla2List.add(result);
                    }
                }
                sla2Object.put("sla2List", sla2List);
            }
        }
        resultData.put("sla2Result", sla2Object);
        return resultData;
    }
    // Do not remove detail, just update shown status
    @RequestMapping(value = "/removeSlaListData", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody void removeSlaListData(@RequestBody Map para){
        JSON json = (JSON)JSON.toJSON(para);
        SlaListDTO listDTO = JSON.toJavaObject(json, SlaListDTO.class);
        listDTO.setIfShown(false);
        slaOTDService.updateListShownStatus(listDTO);
    }
    // 对资源池类项目,ALM点击付款后，SLAservice通过收到的消息将数据存储到一览表
    @RequestMapping(value = "/insertResourcePoolListData", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody void insertResourcePoolListData(@RequestBody Object para) {
        JSON json = (JSON)JSON.toJSON(para);
        AlmListDataDTO insertListData = JSON.toJavaObject(json, AlmListDataDTO.class);
        String type = Constants.PROJECT_TYPE.RESOURCE_POOL_PROJECT;
        //存储一条记录到一览表，同时更新detai表中对应story的关联字段
        int listId = insertListData(insertListData, type);
        if(listId > 0){
            //维护 SLA-PROJECT 关系表，并根据项目名字和时间关联宕机表,dubbo表
            String[] projectName = insertListData.getProjectName().split(",");
            for (String project : projectName) {
                SlaProjectRelDTO dto = new SlaProjectRelDTO();
                dto.setListId(listId);
                dto.setProjectName(project);
                slaProjectRelService.insertSlaProjectRel(dto);

                //outage
                Map outageMap = new HashMap();
                outageMap.put("listId", listId);
                outageMap.put("startDate", insertListData.getStageBeginDate());
                outageMap.put("endDate", insertListData.getStageEndDate());
                outageMap.put("projectName", project);
                slaOutageService.updateSlaListId(outageMap);
                Map dubboMap = new HashMap();
                dubboMap.put("listId", listId);
                dubboMap.put("startDate", insertListData.getStageBeginDate());
                dubboMap.put("endDate", insertListData.getStageEndDate());
                dubboMap.put("projectName", project);
                slaMonitorService.updateSlaListId(dubboMap);
            }
            //bug-sla插入
            insertBugInfo(insertListData, listId);
        } else {
            log.error("SLA记录失败，请确认数据格式。{}", para.toString());
        }

    }

    @RequestMapping(value = "/querySlaData", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JSONObject querySlaData(@RequestBody Object para) {
        JSONObject resultData = new JSONObject();
        JSON json = (JSON)JSON.toJSON(para);
        AlmListDataDTO listData = JSON.toJavaObject(json, AlmListDataDTO.class);
        //task list
        String taskString = listData.getTaskIds();
        List<SlaDetailDTO> detailResult = new ArrayList<SlaDetailDTO>();
        if (taskString != null && !"".equals(taskString)) {
            SlaDetailDTO dto = new SlaDetailDTO();
            dto.setAlmTaskId(taskString);
            detailResult = slaOTDService.getSlaDetail(dto);
        }
        resultData.put("taskList", detailResult);
        //bug list
        List<BugInfoDTO> bugResult = new ArrayList<BugInfoDTO>();
        String projectStr = listData.getProjectName();
        if(projectStr != null && !"".equals(projectStr)){
            String[] projects = projectStr.split(",");
            for (String projectName : projects) {
                Date startDate;
                Date endDate;
                try {
                    startDate = sdf.parse(listData.getStageBeginDate());
                    endDate = sdf.parse(listData.getStageEndDate());
                    List<BugStatisticsInfo> resultBug = jiraService.getBugStatistics(projectName, startDate, endDate);
                    for (BugStatisticsInfo info : resultBug) {
                        BugInfoDTO dto = new BugInfoDTO();
                        dto.setProjectName(projectName);
                        dto.setQuantity(info.getQuantity());
                        dto.setLink(info.getLink());
                        dto.setType(info.getLevel());
                        String level = info.getLevel();
                        int cutPoints = 0;
                        double cutPayment = 0;
                        switch(level){
                            case "blocker":
                                cutPoints = 20 * info.getQuantity();
                                cutPayment = 1;
                                break;
                            case "Critical":
                                cutPoints = 10 * info.getQuantity();
                                cutPayment = 0.5;
                                break;
                            case "Major":
                                cutPoints = 5 * info.getQuantity();
                                cutPayment = 0.3;
                                break;
                        }
                        dto.setCutPoints(cutPoints);
                        dto.setCutPayment(cutPayment);
                        int bugCount = slaBugInfoService.insertBugInfo(dto);
                        if (bugCount == 0){
                            log.error("插入BUG信息出错。{}" ,dto.toString());
                        }
                        bugResult.add(dto);
                    }
                } catch (Exception e) {
                    log.error("日期格式错误。{}", e);
                }
            }
            resultData.put("bugList", bugResult);
        }

        //宕机信息
        List<SlaOutageInterfaceDTO> outageResult = new ArrayList<SlaOutageInterfaceDTO>();
        SlaOutageInterfaceDTO outageDTO = new SlaOutageInterfaceDTO();
        if(projectStr != null && !"".equals(projectStr)){
            String[] projects = projectStr.split(",");
            for (String projectName : projects) {
                String queryStartStr = listData.getStageBeginDate() + " 00:00:00";
                String queryEndStr = listData.getStageEndDate() + " 23:59:59";
                Map map = new HashMap();
                map.put("projectName", projectName);
                List<SlaProjectInfoDTO> projectInfo = slaProjectInfoService.getSlaProjectInfoList(map);
                if(projectInfo != null && projectInfo.size() > 0){
                    String sCode = projectInfo.get(0).getAlmApplicationId();
                    outageDTO = slaInterfaceService.getOutageInfo(sCode, queryStartStr, queryEndStr);
                    outageResult.add(outageDTO);
                }
            }
        }
        resultData.put("outageList", outageResult);
        //dubbo信息
        List<SlaMonitorInterfaceDTO> dubboResult = new ArrayList<SlaMonitorInterfaceDTO>();
        SlaMonitorInterfaceDTO dubboDTO = new SlaMonitorInterfaceDTO();
        if(projectStr != null && !"".equals(projectStr)){
            String[] projects = projectStr.split(",");
            for (String projectName : projects) {
                Map map = new HashMap();
                map.put("projectName", projectName);
                List<SlaProjectInfoDTO> projectInfo = slaProjectInfoService.getSlaProjectInfoList(map);
                String queryStartStr = listData.getStageBeginDate();
                String queryEndStr = listData.getStageEndDate();
                if(projectInfo != null && projectInfo.size() > 0){
                    String sCode = projectInfo.get(0).getAlmApplicationId();
                    dubboDTO = slaInterfaceService.getMointorInfo(sCode, queryStartStr, queryEndStr);
                    dubboResult.add(dubboDTO);
                }
            }
        }
        resultData.put("dubboList", dubboResult);
        return resultData;
    }

    private void insertBugInfo(AlmListDataDTO insertListData, int listId) {
        String project = insertListData.getProjectName();
        if (project != null && !"".equals(project)) {
            //一个SLA可能关联多个项目
            String[] projectNames = project.split(",");
            for (String name : projectNames) {
                try {
                    Date startDate = sdf.parse(insertListData.getStageBeginDate());
                    Date endDate = sdf.parse(insertListData.getStageEndDate());
                    List<BugStatisticsInfo> bugList = jiraService.getBugStatistics(name, startDate, endDate);
                    //每个项目固定有3条记录
                    for (BugStatisticsInfo info : bugList) {
                        BugInfoDTO bug = new BugInfoDTO();
                        bug.setType(info.getLevel());
                        bug.setQuantity(info.getQuantity());
                        bug.setLink(info.getLink());
                        bug.setProjectName(name);
                        bug.setListId(listId);
                        String level = info.getLevel();
                        int cutPoints = 0;
                        double cutPayment = 0;
                        switch(level){
                            case "blocker":
                                cutPoints = 20 * info.getQuantity();
                                cutPayment = 1;
                                break;
                            case "Critical":
                                cutPoints = 10 * info.getQuantity();
                                cutPayment = 0.5;
                                break;
                            case "Major":
                                cutPoints = 5 * info.getQuantity();
                                cutPayment = 0.3;
                                break;
                        }
                        bug.setCutPoints(cutPoints);
                        bug.setCutPayment(cutPayment);
                        int bugId = slaBugInfoService.insertBugInfo(bug);
                        if (bugId == 0){
                            log.error("插入BUG信息出错。{}" ,bug.toString());
                        }
                    }
                } catch (Exception e) {
                    log.error("查询bug时错误。{}", e);
                }
            }
        }
    }

    private int insertResourcePoolData(Object object) {
        JSONObject para = (JSONObject) JSON.toJSON(object);
        SlaListDTO insertDTO = new SlaListDTO();
        insertDTO.setContractStageDesc(para.get("contractStageDesc").toString());
        insertDTO.setType(Constants.PROJECT_TYPE.RESOURCE_POOL_PROJECT);
        insertDTO.setStageBeginDate(para.get("stageBeginDate").toString());
        insertDTO.setStageEndDate(para.get("stageEndDate").toString());

        insertDTO.setReportCreateDate(sdf.format(new Date()));

        insertDTO.setScore(0);
        return 0;
    }

    // 对资源池类项目,ALM点击评价后，SLAservice通过收到的消息将数据存储到详情表
    @RequestMapping(value = "/insertResourcePoolDetailData", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody void insertResourcePoolDetailData(@RequestBody Object para) {
        JSON json = (JSON)JSON.toJSON(para);
        AlmDetailDataDTO detailPara = JSON.toJavaObject(json, AlmDetailDataDTO.class);
        String type = Constants.PROJECT_TYPE.RESOURCE_POOL_PROJECT;
        insertDetailData(detailPara, type);
    }

    @RequestMapping(value = "/insertSysOperListData", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody void insertSysOperListData(Map para) {
        JSON json = (JSON)JSON.toJSON(para);
        AlmListDataDTO insertListData = JSON.toJavaObject(json, AlmListDataDTO.class);
        String type = Constants.PROJECT_TYPE.SYSTEM_OPERTAION_PROJECT;
        String bugList = JSON.toJSONString(para.get("bugList"));
        List<BugInfoDTO> bugs = JSON.parseArray(bugList, BugInfoDTO.class);
        int listId = insertListData(insertListData, type);
        slaBugInfoService.updateBugListId(bugs, listId);
    }

    @RequestMapping(value = "/insertSysOperDetailData", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody void insertSysOperDetailData(Map para) {
        JSON json = (JSON)JSON.toJSON(para);
        SlaDetailDTO insertDetailData = JSON.toJavaObject(json, SlaDetailDTO.class);
        String type = Constants.PROJECT_TYPE.SYSTEM_OPERTAION_PROJECT;
        //insertDetailData(insertDetailData, type);
    }
    // 对系统运维类合同，ALM付款后需要更新List表状态
    @RequestMapping(value = "/updateSysOperStatusByAlmPay", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody void updateSysOperStatusByAlmPay(Map para) {
        JSON json = (JSON)JSON.toJSON(para);
        AlmListDataDTO updateListData = JSON.toJavaObject(json, AlmListDataDTO.class);
        SlaListDTO slaListDTO = new SlaListDTO();
//        boolean status = updateListData.isPaidInAlm();
//        slaListDTO.setIfPaid(status);
        slaListDTO.setTaskId(updateListData.getTaskIds());
        slaOTDService.updateListPaidStatus(slaListDTO);
    }

    public void insertDetailData(AlmDetailDataDTO insertList, String type){
        String[] taskIds = insertList.getAlmTaskId().split(",");
        for (String taskId : taskIds) {
            //try {
                //Issue storyData = basicService.getJiraIssue(taskIds);
                SlaDetailDTO insertDTO = new SlaDetailDTO();
                insertDTO.setProjectName("alm-test");
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
    }
    public int daysBetween(Date smdate,Date bdate) {
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            log.error("日期参数格式错误。{}", e);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
    public int insertListData(AlmListDataDTO dto, String type){
        String[] taskIds = dto.getTaskIds().split(",");
        SlaListDTO insertDTO = new SlaListDTO();
        insertDTO.setContractStageDesc(dto.getContractStageDesc());
        insertDTO.setStageBeginDate(dto.getStageBeginDate());
        insertDTO.setStageEndDate(dto.getStageEndDate());
        insertDTO.setReportCreateDate(sdf.format(new Date()));
        insertDTO.setType(type);
        insertDTO.setIfPaid(true);
        insertDTO.setIfShown(true);
        insertDTO.setProjectName(dto.getProjectName());
        insertDTO.setTaskId(dto.getTaskIds());
        int listId = slaOTDService.insertListData(insertDTO);
        for (int i = 0; i < taskIds.length; i++) {
            SlaDetailDTO updateDTO = new SlaDetailDTO();
            updateDTO.setAlmTaskId(taskIds[i]);
            updateDTO.setListId(listId);
            slaOTDService.updateListIdForDetailData(updateDTO);
        }
        return listId;
    }
}
