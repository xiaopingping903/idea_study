package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.BugInfoDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rsdeep on 2016/12/26.
 */
@Repository("slaBugInfoDAO")
public class SlaBugInfoDAO extends MyBatisDao<BugInfoDTO>{
    public List<BugInfoDTO> search(BugInfoDTO searchDTO){
        return this.getSqlSession().selectList("com.haier.adp.sla.dto.BugInfoDTO.search", searchDTO);
    }
    public int queryCount(BugInfoDTO searchDTO){
        return this.getSqlSession().selectOne("com.haier.adp.sla.dto.BugInfoDTO.queryCount", searchDTO);
    }
    public int insertBugInfo(BugInfoDTO insertDTO){
        return this.getSqlSession().insert("com.haier.adp.sla.dto.BugInfoDTO.insertBugInfoData", insertDTO);
    }
}
