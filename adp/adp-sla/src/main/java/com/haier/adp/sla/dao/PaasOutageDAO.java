package com.haier.adp.sla.dao;

import com.haier.adp.sla.dto.PaasOutageDTO;
import com.haier.adp.sla.dto.SlaOutageDTO;
import com.haier.adp.sla.dto.SlaOutageOperationDetailsDTO;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Repository("paasOutageDAO")
public class PaasOutageDAO extends MyBatisDao<PaasOutageDTO> {
    public  List<PaasOutageDTO> getPaasOutageList(Map map) {
        return  this.getSqlSession().selectList("com.haier.adp.sla.dao.PaasOutageDAO.getPaasOutageList", map);
    }
}
