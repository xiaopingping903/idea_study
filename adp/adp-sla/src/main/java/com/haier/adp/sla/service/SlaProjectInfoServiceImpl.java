package com.haier.adp.sla.service;


import com.haier.adp.sla.dao.*;
import com.haier.adp.sla.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2016/12/22.
 */
@Service(value="slaProjectInfoService")
public class SlaProjectInfoServiceImpl implements SlaProjectInfoService{
    @Autowired
    private SlaProjectInfoDAO slaProjectInfoDAO;

    /**
     * 插入
     * @param slaProjectInfoDTO
     */
    @Override
    public void insertSlaProjectInfo(SlaProjectInfoDTO slaProjectInfoDTO) {
        slaProjectInfoDAO.insertSlaProjectInfo(slaProjectInfoDTO);
    }

    /**
     * 删除
     */
    @Override
    public void delProjectInfo() {
        slaProjectInfoDAO.delProjectInfo();
    }

    @Override
    public List<SlaProjectInfoDTO> getSlaProjectInfoList(Map map) {
        return slaProjectInfoDAO.getSlaProjectInfoList(map);
    }
}
