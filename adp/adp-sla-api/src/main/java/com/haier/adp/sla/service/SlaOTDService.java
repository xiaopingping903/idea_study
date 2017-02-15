package com.haier.adp.sla.service;

import com.haier.adp.jira.dto.StoryTransferInfo;
import com.haier.adp.sla.dto.SlaDetailDTO;
import com.haier.adp.sla.dto.SlaListDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by rsdeep on 2016/12/15.
 */
public interface SlaOTDService {
    List<SlaListDTO> getSlaList(SlaListDTO slaListDTO);
    int queryListCount(SlaListDTO slaListDTO);
    List<SlaDetailDTO> getSysOperSlaList(Date queryStartDate, Date queryEndDate, String system, String projectType);
    int queryDetailCount(SlaDetailDTO slaDetailDTO);
    List<SlaDetailDTO> getSlaDetail(SlaDetailDTO slaDetailDTO);
    List<StoryTransferInfo> getJiraStoriesByProjectAndDate(String productShortName, Date queryStartDate, Date queryEndDate);
    int insertDetailData(SlaDetailDTO detailDTO);
    int insertListData(SlaListDTO listDTO);
    void updateListIdForDetailData(SlaDetailDTO detailDTO);
    void updateListPaidStatus(SlaListDTO listDTO);
    void updateListShownStatus(SlaListDTO listDTO);
    void updateSlaDetail(SlaDetailDTO detailDTO);
}
