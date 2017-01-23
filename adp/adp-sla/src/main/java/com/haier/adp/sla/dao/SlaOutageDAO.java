package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaListDTO;
import com.haier.adp.sla.dto.SlaOutageDTO;
import com.haier.adp.sla.dto.SlaOutageOperationDetailsDTO;
import com.haier.adp.sla.dto.SlaSupplierDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Repository("slaOutageDAO")
public class  SlaOutageDAO extends MyBatisDao<SlaOutageDTO> {
    public int insertSlaOutagelData(SlaOutageDTO slaOutageDTO) {
        return  this.getSqlSession().insert("com.haier.adp.sla.dao.SlaOutageDAO.insertSlaOutagelData", slaOutageDTO);
    }
    public int insertSlaOutageOperationDetailslData(SlaOutageOperationDetailsDTO slaOutageOperationDetailsDTO) {
        return  this.getSqlSession().insert("com.haier.adp.sla.dao.SlaOutageDAO.insertSlaOutageOperationDetailslData", slaOutageOperationDetailsDTO);
    }
    public int updateSlaOutageData(SlaOutageDTO slaOutageDTO) {
        return  this.getSqlSession().update("com.haier.adp.sla.dao.SlaOutageDAO.updateSlaOutageData", slaOutageDTO);
    }
    public int updateSlaListId(SlaOutageDTO slaOutageDTO) {
        return  this.getSqlSession().update("com.haier.adp.sla.dao.SlaOutageDAO.updateSlaListId", slaOutageDTO);
    }
    public  List<SlaOutageDTO> getSlaOutageList(Map map) {
       return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaOutageDAO.getSlaOutageList", map);
    }
    public  SlaOutageDTO getSlaOutage(Map map) {
        return  this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaOutageDAO.getSlaOutage", map);
    }
    public  long getSumOutageTime(Map map){
        return  this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaOutageDAO.getSumOutageTime",map);
    }
    public  long getTotalCutPayment(Map map){
        return  this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaOutageDAO.getTotalCutPayment",map);
    }

    public List<SlaSupplierDTO> getSupplierByOutage(Map map) {
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaOutageDAO.getSupplierByOutage", map);
    }
}
