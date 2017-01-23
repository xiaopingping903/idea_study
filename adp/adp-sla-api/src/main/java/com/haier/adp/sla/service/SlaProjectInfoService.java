package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.SlaProjectInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */
public interface SlaProjectInfoService {
    void insertSlaProjectInfo(SlaProjectInfoDTO slaProjectInfoDTO);
    void delProjectInfo();
    List<SlaProjectInfoDTO> getSlaProjectInfoList(Map map);
}
