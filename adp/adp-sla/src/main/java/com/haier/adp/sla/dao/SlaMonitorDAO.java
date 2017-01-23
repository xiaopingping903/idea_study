package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaMonitorDTO;
import com.haier.adp.sla.dto.SlaSupplierDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("tSlaMonitorDAO")
public class SlaMonitorDAO extends MyBatisDao<SlaMonitorDTO> {
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    public int insert(SlaMonitorDTO record) {
        return this.getSqlSession().insert("com.haier.adp.sla.dao.SlaMonitorDAO.insert",record);
    }

    public SlaMonitorDTO selectByPrimaryKey(Integer id) {
        return null;
    }

    public int updateByPrimaryKey(SlaMonitorDTO record) {
        return 0;
    }

    public List<SlaMonitorDTO> getSlaMonitorList(Map map) {
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaMonitorDAO.getSlaMonitorList",map);
    }
    public SlaMonitorDTO getPercent(Map map){
        return this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaMonitorDAO.getPercent",map);
    }

    public SlaMonitorDTO getSlaMonitor(Map map) {
        return this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaMonitorDAO.getSlaMonitor",map);
    }

    public void updateMonitorData(SlaMonitorDTO slaMonitorDTO) {
        this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaMonitorDAO.updateMonitorData",slaMonitorDTO);
    }
    public void updateSlaListId(SlaMonitorDTO slaMonitorDTO) {
        this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaMonitorDAO.updateSlaListId",slaMonitorDTO);
    }

    public List<SlaSupplierDTO> getSupplierByMonitor(Map newMap) {
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaMonitorDAO.getSupplierByMonitor",newMap);
    }
}