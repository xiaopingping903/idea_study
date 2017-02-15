package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.SlaProjectInfoDTO;
import com.haier.adp.sla.dto.SlaProjectUserInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */
public interface SlaProjectInfoService {
    SlaProjectInfoDTO insertSlaProjectInfo(SlaProjectInfoDTO slaProjectInfoDTO);
    void delProjectInfo(Map map);
    List<SlaProjectInfoDTO> getSlaProjectInfoList(Map map);
    List<SlaProjectInfoDTO> getSlaProjectInfoListByScode(Map map);

    void updateSlaProjectInfo(SlaProjectInfoDTO dto);

    void insertSlaProjectUserInfo(SlaProjectUserInfoDTO userDto);

    void delProjectUserInfo(Integer id);
}
