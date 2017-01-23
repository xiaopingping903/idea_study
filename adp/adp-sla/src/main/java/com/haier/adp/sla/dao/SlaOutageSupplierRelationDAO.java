package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaOutageDTO;
import com.haier.adp.sla.dto.SlaOutageOperationDetailsDTO;
import com.haier.adp.sla.dto.SlaOutageSupplierRelationDTO;
import com.haier.adp.sla.dto.SlaSupplierDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("slaOutageSupplierRelationDAO")
public class SlaOutageSupplierRelationDAO extends MyBatisDao<SlaOutageSupplierRelationDTO> {
    public List<SlaOutageSupplierRelationDTO> getOutageSupplierRelationList(SlaOutageSupplierRelationDTO slaOutageSupplierRelationDTO) {
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaOutageSupplierRelationDAO.getOutageSupplierRelationList",slaOutageSupplierRelationDTO);
    }

    public void insertOutageSupplierRelation(SlaOutageSupplierRelationDTO slaOutageSupplierRelationDTO) {
        this.getSqlSession().insert("com.haier.adp.sla.dao.SlaOutageSupplierRelationDAO.insertOutageSupplierRelation",slaOutageSupplierRelationDTO);
    }

    public void deleteOutageSupplierRelation(SlaOutageSupplierRelationDTO slaOutageSupplierRelationDTO) {
        this.getSqlSession().delete("com.haier.adp.sla.dao.SlaOutageSupplierRelationDAO.deleteOutageSupplierRelation",slaOutageSupplierRelationDTO);

    }
}