package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */
public interface SlaOutageService {
    void insertSlaOutagelData(SlaOutageDTO slaOutageDTO);
    void updateSlaOutageData(Map map);
    void updateSlaListId(Map map);
    List<SlaOutageDTO> getSlaOutageList(Map map);
    int querySlaOutageListCount(Map map);
    List<SlaSupplierDTO> getSlaSupplierList(Map map);
    List<PaasOutageDTO> getPaasOutageList(Map map);
    long getSumOutageTime(Map map);
    List<SlaProjectInfoDTO> getSlaProjectInfoList(Map map) ;
    int getTotalTime(List<SlaOutageDTO> slaOutageList);
    //给alm返回宕机稳定性等信息
    SlaOutageInterfaceDTO getOutageInfo(String almShortName,int tSlaListId,String fromDate,String toDate);
    SlaOutageDTO getSlaOutage(int id);
    List<SlaOutageDTO> getServiceNum(Map map);
    List<SlaProjectUserInfoDTO> getPMList(Map map);
    Map getRole(String adpAccountId);
    int getNotSetSupplierOutageNum(Map map);
}
