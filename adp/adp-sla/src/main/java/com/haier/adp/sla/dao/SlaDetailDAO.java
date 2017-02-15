package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaDetailDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Repository("slaDetailDAO")
public class SlaDetailDAO extends MyBatisDao<SlaDetailDTO>{
    public int insertDetailData(SlaDetailDTO detail){
        this.getSqlSession().insert("com.haier.adp.sla.dao.SlaDetailDAO.insertDetailData", detail);
        int id = detail.getId();
        return id;
    }
    public void updateListIdForDetailData(SlaDetailDTO detail){
        this.getSqlSession().update("com.haier.adp.sla.dao.SlaDetailDAO.updateListIdForDetailData", detail);
    };
    public void updateSlaDetail(SlaDetailDTO detail){
        this.getSqlSession().update("com.haier.adp.sla.dao.SlaDetailDAO.updateSlaDetail", detail);
    };
    public List<SlaDetailDTO> search(SlaDetailDTO searchDTO){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaDetailDAO.search", searchDTO);
    }
    public int queryCount(SlaDetailDTO searchDTO){
        return this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaDetailDAO.queryCount", searchDTO);
    }
    public List<SlaDetailDTO> getSlaALMJIRARelationList(Map map){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaDetailDAO.getSlaALMJIRARelationList",map);
    }
    public void updateRelate(SlaDetailDTO slaDetailDTO){
        this.getSqlSession().update("com.haier.adp.sla.dao.SlaDetailDAO.updateRelate",slaDetailDTO);
    }
}
