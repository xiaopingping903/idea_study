package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaOutageOperationDetailsDTO;
import com.haier.adp.sla.dto.SlaSupplierDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("slaSupplierDAO")
public class SlaSupplierDAO  extends MyBatisDao<SlaOutageOperationDetailsDTO> {
    public List<SlaSupplierDTO> getSlaSupplierList(Map map){
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaSupplierDAO.getSlaSupplierList",map);
    }
}