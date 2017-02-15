package com.haier.adp.sla.service;

import com.haier.adp.sla.dao.SlaProjectUserInfoDAO;
import com.haier.adp.sla.dto.SlaProjectUserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2017/2/6.
 */
public class SlaProjectUserInfoServiceImpl implements SlaProjectUserInfoService{
    @Autowired
    private SlaProjectUserInfoDAO slaProjectUserInfoDAO;
    @Override
    public List<SlaProjectUserInfoDTO> getSlaProjectUserInfo(Map queryMap) {
        List<SlaProjectUserInfoDTO> userInfo = new ArrayList<SlaProjectUserInfoDTO>();
        userInfo = slaProjectUserInfoDAO.getSlaProjectUserInfo(queryMap);
        return userInfo;
    }
}
