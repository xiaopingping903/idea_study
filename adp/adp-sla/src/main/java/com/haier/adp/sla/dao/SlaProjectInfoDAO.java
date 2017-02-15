package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaProjectInfoDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("slaProjectInfoDAO")
public class SlaProjectInfoDAO extends MyBatisDao<SlaProjectInfoDTO> {
    public List<SlaProjectInfoDTO> getSlaProjectInfoList(Map map){
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaProjectInfoDAO.getSlaProjectInfoList",map);
    }
    public List<SlaProjectInfoDTO> getSlaProjectInfoListByScode(Map map){
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaProjectInfoDAO.getSlaProjectInfoListByScode",map);
    }
    public int insertSlaProjectInfo(SlaProjectInfoDTO slaProjectInfoDTO){
        return  this.getSqlSession().insert("com.haier.adp.sla.dao.SlaProjectInfoDAO.insertSlaProjectInfo",slaProjectInfoDTO);
    }

    public void delProjectInfo(Map map) {
        this.getSqlSession().delete("com.haier.adp.sla.dao.SlaProjectInfoDAO.delProjectInfo",map);
    }

    public void updateSlaProjectInfo(SlaProjectInfoDTO dto) {
        this.getSqlSession().update("com.haier.adp.sla.dao.SlaProjectInfoDAO.updateSlaProjectInfo",dto);
    }
}