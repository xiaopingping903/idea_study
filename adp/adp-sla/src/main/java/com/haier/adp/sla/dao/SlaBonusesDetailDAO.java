package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaBonusesDetailDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("slaBonusesDetailDAO")
public class SlaBonusesDetailDAO extends MyBatisDao<SlaBonusesDetailDTO>{
    public List<SlaBonusesDetailDTO> getBonuseItemList(Map map){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaBonusesDetailDAO.getBonuseItemList",map);
    }

    public void insertSlaBonusesDetail(SlaBonusesDetailDTO slaBonusesDetailDTO) {
        this.getSqlSession().insert("com.haier.adp.sla.dao.SlaBonusesDetailDAO.insertSlaBonusesDetail",slaBonusesDetailDTO);
    }

    public void deleteSlaBonusesDetail(Map map){
        this.getSqlSession().delete("com.haier.adp.sla.dao.SlaBonusesDetailDAO.deleteSlaBonusesDetail",map);
    }

    public void delSlaBonusesDetail(Map map){
        this.getSqlSession().update("com.haier.adp.sla.dao.SlaBonusesDetailDAO.delSlaBonusesDetail",map);
    }
    public List<SlaBonusesDetailDTO> getDetailList(Map map){
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaBonusesDetailDAO.getDetailList",map);
    }
}