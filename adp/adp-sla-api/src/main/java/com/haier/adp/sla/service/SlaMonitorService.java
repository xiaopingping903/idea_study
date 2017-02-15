package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.SlaMonitorDTO;
import com.haier.adp.sla.dto.SlaMonitorInterfaceDTO;
import com.haier.adp.sla.dto.SlaProjectUserInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */
public interface SlaMonitorService {
   List<SlaMonitorDTO> getSlaMonitorList(Map map) ;
    void updateSlaListId(Map map);
    int querySlaMonitorListCount(Map map);

    SlaMonitorDTO getPercent(Map map);

    void updateSlaMonitorData(Map map);

    //给alm返回dubbo异常稳定性等信息
    SlaMonitorInterfaceDTO getMointorInfo(String almShortName,int tSlaListId);

    void insertSlaMonitor(SlaMonitorDTO slaMonitorDTO);

    String updateSupplierPercent(Map map);

   List<SlaMonitorDTO> getServiceNum(Map mmm);

    List<SlaProjectUserInfoDTO> getPMMonitorList(Map map);
}
