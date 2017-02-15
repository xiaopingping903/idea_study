package com.haier.adp.sla.dao;
import com.haier.adp.sla.dto.SlaBonusesDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("slaBonusesDAO")
public class SlaBonusesDAO extends MyBatisDao<SlaBonusesDTO>{
    public List<SlaBonusesDTO> getSlaBonusesList(Map map){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaBonusesDAO.getSlaBonusesList",map);
    }

    public int insertSlaBonusesDTO(SlaBonusesDTO slaBonusesDTO){
        return this.getSqlSession().insert("com.haier.adp.sla.dao.SlaBonusesDAO.insertSlaBonusesDTO",slaBonusesDTO);
    }

    public void updateSlaBonuses(SlaBonusesDTO slaBonusesDTO){
        this.getSqlSession().update("com.haier.adp.sla.dao.SlaBonusesDAO.updateSlaBonuses",slaBonusesDTO);
    }

    public int delSlaBonuse(Map map){
        return this.getSqlSession().update("com.haier.adp.sla.dao.SlaBonusesDAO.delSlaBonuse",map);
    }

    public List<SlaBonusesDTO> getBonuseInfo(Map map) {
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaBonusesDAO.getBonuseInfo",map);
    }

    public SlaBonusesDTO getSumScoreAndMoney(Map map) {
        return this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaBonusesDAO.getSumScoreAndMoney",map);
    }
    public SlaBonusesDTO updateSlaListId(SlaBonusesDTO dto) {
        return this.getSqlSession().selectOne("com.haier.adp.sla.dao.SlaBonusesDAO.updateSlaListId",dto);
    }
}