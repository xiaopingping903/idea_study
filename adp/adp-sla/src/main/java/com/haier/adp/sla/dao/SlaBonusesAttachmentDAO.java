package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaBonusesAttachmentDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("slaBonusesAttachmentDTO")
public class SlaBonusesAttachmentDAO extends MyBatisDao<SlaBonusesAttachmentDTO>{

    public void insertSlaBonusesAttachmentDTO(SlaBonusesAttachmentDTO sbad) {
        this.getSqlSession().insert("com.haier.adp.sla.dao.SlaBonusesAttachmentDAO.insertSlaBonusesAttachmentDTO",sbad);
    }

    public void deleteSlaBonusesAttachment(Map map) {
        this.getSqlSession().delete("com.haier.adp.sla.dao.SlaBonusesAttachmentDAO.deleteSlaBonusesAttachment",map);
    }

    public List<SlaBonusesAttachmentDTO> getBonuseAttachmentList(Map mmm) {
        return this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaBonusesAttachmentDAO.getBonuseAttachmentList",mmm);
    }
}