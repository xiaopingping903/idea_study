package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaProjectUserInfoDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2017/1/16.
 */
@Repository(value="slaProjectUserInfoDAO")
public class SlaProjectUserInfoDAO extends MyBatisDao<SlaProjectUserInfoDTO>{
    public List<SlaProjectUserInfoDTO> getSlaProjectUserInfo(Map map) {
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaProjectUserInfoDAO.getUserInfo",map);
    }
    public int insertSlaProjectUserInfo(SlaProjectUserInfoDTO slaProjectUserInfoDTO){
        return this.getSqlSession().insert("com.haier.adp.sla.dao.SlaProjectUserInfoDAO.insertInfoData",slaProjectUserInfoDTO);
    }

    public void delRoleRel(Map map) {
        this.getSqlSession().delete("com.haier.adp.sla.dao.SlaProjectUserInfoDAO.delRoleRel",map);
    }
    public List<SlaProjectUserInfoDTO> getPMList(Map map){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaProjectUserInfoDAO.getPMList",map);
    }
    public List<SlaProjectUserInfoDTO> getPMMonitorList(Map map){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaProjectUserInfoDAO.getPMMonitorList",map);
    }
    public List<SlaProjectUserInfoDTO> getUserInfo(Map map) {
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaProjectUserInfoDAO.getUserInfo",map);
    }
}
