package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaMonitorDTO;
import com.haier.adp.sla.dto.SlaSupplierDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("tSlaMonitorDAO")
public class SlaMonitorDAO extends MyBatisDao<SlaMonitorDTO> {
    public int insert(SlaMonitorDTO record){
        return this.getSqlSession().insert("com.haier.adp.sla.dao.SlaMonitorDAO.insert",record);
    }
    public List<SlaMonitorDTO> getSlaMonitorList(Map map){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaMonitorDAO.getSlaMonitorList",map);
    }
    public SlaMonitorDTO getPercent(Map map){
        return this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaMonitorDAO.getPercent",map);
    }

    public SlaMonitorDTO getSlaMonitor(Map map){
        return this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaMonitorDAO.getSlaMonitor",map);
    }

    public void updateMonitorData(SlaMonitorDTO slaMonitorDTO) throws DataAccessException {
        this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaMonitorDAO.updateMonitorData",slaMonitorDTO);
    }
    public void updateSlaListId(SlaMonitorDTO slaMonitorDTO) throws DataAccessException {
        this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaMonitorDAO.updateSlaListId",slaMonitorDTO);
    }

    public List<SlaSupplierDTO> getSupplierByMonitor(Map newMap) throws DataAccessException {
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaMonitorDAO.getSupplierByMonitor",newMap);
    }

    public void updateSupplierPercent(Map map) throws DataAccessException {
        this.getSqlSession().update("com.haier.adp.sla.dao.SlaMonitorDAO.updateSupplierPercent",map);
    }
    public List<SlaMonitorDTO> getServiceNum(Map map) throws DataAccessException {
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaMonitorDAO.getServiceNum",map);
    }
}