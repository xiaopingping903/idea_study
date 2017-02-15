package com.haier.adp.sla.service;


import com.haier.adp.sla.dao.SlaALMJIRARelationHistoryDAO;
import com.haier.adp.sla.dao.SlaALMJIRARelationLogDAO;
import com.haier.adp.sla.dao.SlaDetailDAO;
import com.haier.adp.sla.dto.SlaALMJIRARelationHistoryDTO;
import com.haier.adp.sla.dto.SlaALMJIRARelationLogDTO;
import com.haier.adp.sla.dto.SlaDetailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Administrator on 2016/12/22.
 */
@Service(value="slaALMJIRARelationHistoryService")
public class SlaALMJIRARelationHistoryServiceImpl implements SlaALMJIRARelationHistoryService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SlaALMJIRARelationHistoryDAO slaALMJIRARelationHistoryDAO;
    @Autowired
    private SlaALMJIRARelationLogDAO slaALMJIRARelationLogDAO;
    @Autowired
    private SlaDetailDAO slaDetailDAO;

    @Override
    public List<SlaDetailDTO> getSlaALMJIRARelationList(Map map) {
        List<SlaDetailDTO> list=new ArrayList<SlaDetailDTO>();
        try{
            list=slaDetailDAO.getSlaALMJIRARelationList(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return list;
    }

    @Override
    public String updateRelation(Map map) {
        String ifSuccess="1";
        try {
            SlaDetailDTO slaDetailDTO=slaDetailDAO.getSlaALMJIRARelationList(map).get(0);
            slaDetailDTO.setJiraEpicId(map.get("newjiraEpicId")+"");
            slaDetailDTO.setJiraStoryId(map.get("newjiraStoryId")+"");
            slaDetailDTO.setTaskTitle(map.get("taskTitle")+"");
            slaDetailDTO.setApplyPD(Integer.parseInt(map.get("applyPD").toString()));
            slaDetailDTO.setAssignPerson(map.get("assignPerson")+"");
            slaDetailDTO.setManagerPrice(Integer.parseInt(map.get("managerPrice").toString()));
            slaDetailDTO.setReleaseTask(Boolean.parseBoolean(map.get("releaseTask").toString()));
            slaDetailDTO.setRequestActualDate(map.get("requestActualDate")+"");
            slaDetailDTO.setRequestCloseDate(map.get("requestCloseDate")+"");
            slaDetailDTO.setRequestPlanDate(map.get("requestPlanDate")+"");
            slaDetailDTO.setTaskDesc(map.get("TaskDesc")+"");
            //更新sla
            slaDetailDAO.updateSlaDetail(slaDetailDTO);
            //插入历史表记录
            SlaALMJIRARelationHistoryDTO slaALMJIRARelationHistoryDTO=new SlaALMJIRARelationHistoryDTO();
            slaALMJIRARelationHistoryDTO.setJiraStoryId(map.get("newjiraStoryId")+"");
            slaALMJIRARelationHistoryDTO.setJiraEpicId(map.get("newjiraEpicId")+"");
            slaALMJIRARelationHistoryDTO.setIfDel("0");
            slaALMJIRARelationHistoryDTO.setAlmRequestId(slaDetailDTO.getAlmRequestId());
            slaALMJIRARelationHistoryDTO.setAlmTaskId(slaDetailDTO.getAlmTaskId());
            slaALMJIRARelationHistoryDTO.setAlmUpdateStatus("0");
            slaALMJIRARelationHistoryDTO.setCreateTime(new Timestamp(new Date().getTime()));
            slaALMJIRARelationHistoryDTO.setJiraUpdateStatus("0");
            slaALMJIRARelationHistoryDTO.setOperator(map.get("operator")+"");
            slaALMJIRARelationHistoryDTO.setOperatorId(map.get("operator_id")+"");
            slaALMJIRARelationHistoryDTO.setSlaUpdateStatus("1");
            slaALMJIRARelationHistoryDTO.setSlaUpdateTime(new Timestamp(new Date().getTime()));
            slaALMJIRARelationHistoryDTO.setTaskTitle(map.get("taskTitle")+"");
            slaALMJIRARelationHistoryDAO.insertRelation(slaALMJIRARelationHistoryDTO);
        }catch (Exception e){
            e.printStackTrace();
            ifSuccess="0";
            logger.info("数据访问资源失败");
        }
        return ifSuccess;
    }

    @Override
    public List<SlaALMJIRARelationHistoryDTO> getRelationList(Map map) {
        List<SlaALMJIRARelationHistoryDTO> list=new ArrayList<SlaALMJIRARelationHistoryDTO>();
        try{
            list=slaALMJIRARelationHistoryDAO.getRelationList(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return list;
    }

    @Override
    public void updateJiraRelation(SlaALMJIRARelationHistoryDTO dto) {
        try{
            slaALMJIRARelationHistoryDAO.updateJiraRelation(dto);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
    }

    @Override
    public void insertLog(SlaALMJIRARelationLogDTO slaLogDTO) {
        try{
             slaALMJIRARelationLogDAO.insertLog(slaLogDTO);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
    }
}
