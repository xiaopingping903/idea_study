package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.SlaProjectUserInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2017/2/6.
 */
public interface SlaProjectUserInfoService {
    List<SlaProjectUserInfoDTO> getSlaProjectUserInfo(Map queryMap);
}
