package com.haier.adp.sla.dao;


import com.haier.adp.sla.dto.SlaALMJIRARelationHistoryDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("slaALMJIRARelationHistoryDAO")
public class SlaALMJIRARelationHistoryDAO extends MyBatisDao<SlaALMJIRARelationHistoryDTO>{

    public void insertRelation(SlaALMJIRARelationHistoryDTO slaALMJIRARelationHistoryDTO) {
        this.getSqlSession().insert("com.haier.adp.sla.dao.SlaALMJIRARelationHistoryDAO.insertRelation",slaALMJIRARelationHistoryDTO);
    }
    public List<SlaALMJIRARelationHistoryDTO> getRelationList(Map map){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaALMJIRARelationHistoryDAO.getRelationList",map);
    }

    public void updateJiraRelation(SlaALMJIRARelationHistoryDTO dto){
        this.getSqlSession().update("com.haier.adp.sla.dao.SlaALMJIRARelationHistoryDAO.updateJiraRelation",dto);
    }
}