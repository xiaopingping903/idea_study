package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaOutageOperationDetailsDTO;
import com.haier.adp.sla.dto.SlaProjectInfoDTO;
import com.haier.adp.sla.dto.SlaSupplierDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("slaProjectInfoDAO")
public class SlaProjectInfoDAO extends MyBatisDao<SlaProjectInfoDTO> {
    public List<SlaProjectInfoDTO> getSlaProjectInfoList(Map map) {
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaProjectInfoDAO.getSlaProjectInfoList",map);
    }
    public void insertSlaProjectInfo(SlaProjectInfoDTO slaProjectInfoDTO){
        this.getSqlSession().insert("com.haier.adp.sla.dao.SlaProjectInfoDAO.insertSlaProjectInfo",slaProjectInfoDTO);
    }

    public void delProjectInfo() {
        this.getSqlSession().delete("com.haier.adp.sla.dao.SlaProjectInfoDAO.delProjectInfo");
    }
}