package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaProjectRelDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rsdeep on 2016/12/27.
 */
@Repository("slaProjectRelDAO")
public class SlaProjectRelDAO extends MyBatisDao<SlaProjectRelDTO> {
    public List<SlaProjectRelDTO> search(SlaProjectRelDTO searchDTO){
        return this.getSqlSession().selectList("com.haier.adp.sla.dto.SlaProjectRelDTO.search", searchDTO);
    }
    public int queryCount(SlaProjectRelDTO searchDTO){
        return this.getSqlSession().selectOne("com.haier.adp.sla.dto.SlaProjectRelDTO.queryCount", searchDTO);
    }
    public int insertSlaProjectRel(SlaProjectRelDTO insertDTO){
        return this.getSqlSession().insert("com.haier.adp.sla.dto.SlaProjectRelDTO.insertSlaProjectRel", insertDTO);
    }
}
