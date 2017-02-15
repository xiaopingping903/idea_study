package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.SlaBonuseInterfaceDTO;
import com.haier.adp.sla.dto.SlaMonitorInterfaceDTO;
import com.haier.adp.sla.dto.SlaOutageInterfaceDTO;
import com.haier.adp.sla.dto.StoryInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface SlaInterfaceService {
    //根据 almApplicationSCode s码  fromDate 开始时间,toDate 结束时间 supplierId供应商id 得到 宕机时间、宕机稳定性、宕机次数、宕机扣款人天
    SlaOutageInterfaceDTO getOutageInfo(String almApplicationSCode, String fromDate, String toDate,String supplierId);
    //根据 almApplicationSCode s码  fromDate 开始时间,toDate 结束时间 supplierId供应商id 得到 异常总次数、异常失败次数、异常成功次数、稳定性、扣款人天
    SlaMonitorInterfaceDTO getMointorInfo(String almApplicationSCode, String fromDate, String toDate,String supplierId);
    //根据应用简称almShortName、fromDate 开始时间,toDate 结束时间得到已经关闭的任务列表
    List<StoryInfo> getClosedStoryList(String almShortName, Date fromDate, Date toDate, int pageNo, int pageSize);
    //根据 almApplicationSCode s码  fromDate 开始时间,toDate 结束时间 supplierId供应商id 得到奖励的分数、人天
    SlaBonuseInterfaceDTO getBonuseInfo(String almApplicationSCode, String fromDate, String toDate,String supplierId);
}
