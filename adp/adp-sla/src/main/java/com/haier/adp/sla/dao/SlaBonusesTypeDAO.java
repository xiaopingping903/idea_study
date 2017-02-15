package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaBonusesTypeDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("slaBonusesTypeDAO")
public class SlaBonusesTypeDAO extends MyBatisDao<SlaBonusesTypeDTO>{

    public List<SlaBonusesTypeDTO> getBonusesTypeList(Map map){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaBonusesTypeDAO.getBonusesTypeList",map);
    }
}