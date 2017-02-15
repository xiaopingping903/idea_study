package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.SlaALMJIRARelationHistoryDTO;
import com.haier.adp.sla.dto.SlaALMJIRARelationLogDTO;
import com.haier.adp.sla.dto.SlaDetailDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */
public interface SlaALMJIRARelationHistoryService {
    List<SlaDetailDTO> getSlaALMJIRARelationList(Map map);

    String updateRelation(Map map);
    List<SlaALMJIRARelationHistoryDTO> getRelationList(Map map);

    void updateJiraRelation(SlaALMJIRARelationHistoryDTO dto);

    void insertLog(SlaALMJIRARelationLogDTO slaLogDTO);
}
