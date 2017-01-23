package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaListDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Repository("slaListDAO")
public class SlaListDAO extends MyBatisDao<SlaListDTO>{
    public int insertListData(SlaListDTO slaListDTO){
        this.getSqlSession().insert("com.haier.adp.sla.dto.SlaListDTO.insertListData", slaListDTO);
        int id = slaListDTO.getId();
        return id;
    }
    public void updateListPaidStatus(SlaListDTO slaListDTO){
        this.getSqlSession().update("com.haier.adp.sla.dto.SlaListDTO.updateListPaidStatus", slaListDTO);
    }
    public void updateListShownStatus(SlaListDTO slaListDTO){
        this.getSqlSession().update("com.haier.adp.sla.dto.SlaListDTO.updateListShownStatus", slaListDTO);
    }
    public List<SlaListDTO> search(SlaListDTO searchDTO){
        return this.getSqlSession().selectList("com.haier.adp.sla.dto.SlaListDTO.search", searchDTO);
    }
    public int queryCount(SlaListDTO searchDTO){
        return this.getSqlSession().selectOne("com.haier.adp.sla.dto.SlaListDTO.queryCount", searchDTO);
    }
}
