package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.SlaOutageDTO;
import com.haier.adp.sla.dto.SlaOutageOperationDetailsDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Repository("slaOutageOperationDetailsDAO")
public class SlaOutageOperationDetailsDAO extends MyBatisDao<SlaOutageOperationDetailsDTO> {
    public int insertSlaOutageOperationDetailsData(SlaOutageOperationDetailsDTO slaOutageOperationDetailsDTO) {
        return  this.getSqlSession().insert("com.haier.adp.sla.dao.SlaOutageOperationDetailsDAO.insertSlaOutageOperationDetailsData", slaOutageOperationDetailsDTO);
    }

    public  List<SlaOutageOperationDetailsDTO> getSlaOutageOperationDetailsList() {
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.SlaOutageOperationDetailsDAO.getSlaOutageOperationDetailsList");
    }
}
