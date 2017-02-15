package com.haier.adp.sla.dao;


import com.haier.adp.sla.dto.SlaALMJIRARelationLogDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

@Repository("slaALMJIRARelationLogDAO")
public class SlaALMJIRARelationLogDAO extends MyBatisDao<SlaALMJIRARelationLogDAO>{

    public void insertLog(SlaALMJIRARelationLogDTO slaLogDTO){
        this.getSqlSession().insert("com.haier.adp.sla.dao.SlaALMJIRARelationLogDAO.insertLog",slaLogDTO);
    }
}