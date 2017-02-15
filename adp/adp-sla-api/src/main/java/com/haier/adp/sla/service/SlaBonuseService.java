package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.SlaBonusesDTO;
import com.haier.adp.sla.dto.SlaBonusesTypeDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */
public interface SlaBonuseService {
    List<SlaBonusesDTO> getSlaBonusesList(Map map);

    List<SlaBonusesTypeDTO> getBonusesTypeList();

    String saveAddSlaBonuse(Map map);

    String delSlaBonuse(Map map);

    List<SlaBonusesDTO> getBonuseInfo(String almApplicationSCode, String fromDate, String toDate);

    void updateSlaListId(Map map);

    String ifAddBonuse(Map map);

    Map getRole(String adpAccountId);
}
