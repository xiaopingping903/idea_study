package com.haier.adp.sla.dao;

import com.alibaba.dubbo.config.annotation.Service;
import com.haier.adp.sla.dto.SlaDetailDTO;
import com.haier.adp.sla.dto.SlaListDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Repository("slaDetailDAO")
public class SlaDetailDAO extends MyBatisDao<SlaDetailDTO>{
    public int insertDetailData(SlaDetailDTO detail){
        this.getSqlSession().insert("com.haier.adp.sla.dto.SlaDetailDTO.insertDetailData", detail);
        int id = detail.getId();
        return id;
    }
    public void updateListIdForDetailData(SlaDetailDTO detail){
        this.getSqlSession().update("com.haier.adp.sla.dto.SlaDetailDTO.updateListIdForDetailData", detail);
    };
    public List<SlaDetailDTO> search(SlaDetailDTO searchDTO){
        return this.getSqlSession().selectList("com.haier.adp.sla.dto.SlaDetailDTO.search", searchDTO);
    }
    public int queryCount(SlaDetailDTO searchDTO){
        return this.getSqlSession().selectOne("com.haier.adp.sla.dto.SlaDetailDTO.queryCount", searchDTO);
    }
}
