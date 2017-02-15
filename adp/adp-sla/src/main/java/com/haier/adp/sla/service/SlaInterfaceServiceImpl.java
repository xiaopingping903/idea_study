package com.haier.adp.sla.service;


import com.haier.adp.adp_jenkins.JenkinsService;
import com.haier.adp.jira.MetricJiraService;
import com.haier.adp.jira.impl.MetricJiraServiceImpl;
import com.haier.adp.sla.dao.SlaBonusesDAO;
import com.haier.adp.sla.dao.SlaMonitorDAO;
import com.haier.adp.sla.dao.SlaOutageDAO;
import com.haier.adp.sla.dao.SlaSupplierDAO;
import com.haier.adp.sla.dto.*;
import com.haier.adp.sla.util.Ideone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/12/22.
 */
@Service("slaInterfaceService")
public class SlaInterfaceServiceImpl implements SlaInterfaceService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    MetricJiraService metricJiraService=new MetricJiraServiceImpl();
    JenkinsService jenkinsService=new JenkinsService();
    @Autowired
    private SlaOutageDAO slaOutageDAO;
    @Autowired
    private SlaMonitorDAO slaMonitorDAO;
    @Autowired
    private SlaSupplierDAO slaSupplierDAO;
    @Autowired
    private SlaBonusesDAO slaBonusesDAO;

    /**
     *   @param  almApplicationSCode s码  fromDate 宕机开始时间,toDate 宕机结束时间
     *根据s码和时间段查宕机信息
     * @return
     */
    @Override
    public SlaOutageInterfaceDTO getOutageInfo(String almApplicationSCode,String fromDate,String toDate,String supplierId ) {
        Map map=new HashMap();
        map.put("almApplicationId",almApplicationSCode);
        map.put("fromDate",fromDate);
        map.put("toDate",toDate);
        SlaOutageInterfaceDTO slaOutageInterfaceDTO=new SlaOutageInterfaceDTO();
        //查询是否没有设置供应商的宕机信息
        int num= slaOutageDAO.getNotSetSupplierOutageNum(map);
        if(num!=0){
            slaOutageInterfaceDTO.setMessage("有宕机信息没有确认，请项目经理确认");
        }else{
            List<SlaOutageDTO> slaOutageList=new ArrayList<SlaOutageDTO>();
            map.put("type","1");
            DecimalFormat df=new DecimalFormat("##.##");
            try {
                slaOutageList = slaOutageDAO.getSlaOutageList((map));
                if (slaOutageList.size() > 0) {
                    float totalTime = (float) getTotalTime(slaOutageList) / 1000 / 60;
                    slaOutageInterfaceDTO.setTotalTime(Integer.parseInt(df.format(totalTime)));//总的宕机时间
                    try {
                        float time = (float) (Timestamp.valueOf(map.get("toDate").toString()).getTime() - Timestamp.valueOf(map.get("fromDate").toString()).getTime()) / 1000 / 60;
                        float percent = 1 - (float) (long) totalTime / time;
                        slaOutageInterfaceDTO.setPercent(df.format(percent * 100) + "%");//宕机稳定性
                        slaOutageInterfaceDTO.setTotalNum(slaOutageList.size());//总的宕机次数
                        if (null == slaOutageDAO.getTotalCutPayment(map)) {

                        } else {
                            double cutPayment = (double) slaOutageDAO.getTotalCutPayment(map);
                            //总的扣款人天
                            //计算由alm供应商导致宕机的时间、次数、扣款
                            map.put("supplierId", supplierId);
                            double scurPayment = slaOutageDAO.getScutPayment(map);
                            List<SlaOutageDTO> SlaOutageDTOlist = slaOutageDAO.getSupplierTimeByScode(map);
                            float ssTime = (float) getTotalTime(SlaOutageDTOlist) / 1000 / 60;
                            slaOutageInterfaceDTO.setSTime(Integer.parseInt(df.format(ssTime)));//对应供应商的宕机时间
                            slaOutageInterfaceDTO.setSNum(SlaOutageDTOlist.size());//对应供应商的宕机次数
                            //此处计算考核周期宕机时间 ≧ 34h * (考核周期 / 年度12个月)
                            float standard_time = 36 * (time / (12 * 30 * 24 * 60)) * 60;
                            if (totalTime >= standard_time) {
                                slaOutageInterfaceDTO.setTotalCutPayment(cutPayment + 5);//总的扣款人天
                                List<SlaSupplierDTO> slaSupplierDTOList = slaOutageDAO.getSupplierByScode(map);
                                int tnum = 0;//计算所有供应商的宕机时间和
                                if (slaSupplierDTOList.size() > 0) {
                                    for (int i = 0; i < slaSupplierDTOList.size(); i++) {
                                        SlaSupplierDTO dto = slaSupplierDTOList.get(i);
                                        map.put("supplierId", dto.getSupplierId());
                                        tnum = tnum + getTotalTime(slaOutageDAO.getSupplierTimeByScode(map));
                                    }
                                }
                                double tcurp = scurPayment;
                                if (tnum != 0) {
                                    tcurp = tcurp + 5 * (ssTime * 1000 * 60 / tnum);
                                }
                                slaOutageInterfaceDTO.setStotalCutPayment(tcurp);//对应供应商的扣款
                            } else {
                                slaOutageInterfaceDTO.setTotalCutPayment(cutPayment);//总的扣款人天
                                slaOutageInterfaceDTO.setStotalCutPayment(scurPayment);//对应供应商的扣款
                            }
                            slaOutageInterfaceDTO.setMessage("正常返回宕机考核数据");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    logger.info("日期格式不对");
                }
            }
            }catch (Exception e){
                e.printStackTrace();
                logger.info("数据访问资源失败");
            }
        }
        return slaOutageInterfaceDTO;
    }
    public int getTotalTime(List<SlaOutageDTO> slaOutageList) {
        Ideone ideone=new Ideone();
        int totalTime=0;
        try {

            totalTime = ideone.getTotalTime(slaOutageList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalTime;
    }


    /**
     *   @param  almApplicationSCode s码  fromDate 宕机开始时间,toDate 宕机结束时间
     *根据s码和时间段查宕机信息
     * @return
     */
    @Override
    public SlaMonitorInterfaceDTO getMointorInfo(String almApplicationSCode,String fromDate,String toDate,String supplierId) {
        Map map = new HashMap();
        map.put("almApplicationId", almApplicationSCode);
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        SlaMonitorInterfaceDTO slaMonitorInterfaceDTO = new SlaMonitorInterfaceDTO();
        try{
            SlaMonitorDTO slaMonitorDTO = slaMonitorDAO.getPercent(map);
            if(slaMonitorDTO==null){
                //返回null
            }else{
                slaMonitorInterfaceDTO.setInvokedTotal(new Double(slaMonitorDTO.getInvokedTotal()).intValue());//总次数
                slaMonitorInterfaceDTO.setInvokedSuccessTotal(new Double(slaMonitorDTO.getInvokedSuccessTotal()).intValue());//成功次数
                slaMonitorInterfaceDTO.setInvokedFailTotal(new Double(slaMonitorDTO.getInvokedFailTotal()).intValue());//失败次数
                DecimalFormat df=new DecimalFormat("##.##");
                float percent = (float) slaMonitorDTO.getInvokedFailTotal() / slaMonitorDTO.getInvokedTotal();
                slaMonitorInterfaceDTO.setPercent(df.format(percent * 100) + "%");//稳定性
                if (percent >= 0.01) {
                    Map mm=new HashMap();
                    mm.put("projectId",almApplicationSCode);
                    mm.put("supplierId",supplierId);
                    //获取供应商所占比例
                    List<SlaSupplierDTO> slaSupplierList=new ArrayList<SlaSupplierDTO>();
                    slaSupplierList=slaSupplierDAO.getSlaSupplierList(mm);
                    if(slaSupplierList.size()>0){
                        float percentSupplier=slaSupplierList.get(0).getPercent();
                        if("null".equals(String.valueOf(percentSupplier))){
                            mm.put("supplierId",null);
                            slaSupplierList=slaSupplierDAO.getSlaSupplierList(mm);
                            percentSupplier=(float) 1/slaSupplierList.size();
                        }
                        slaMonitorInterfaceDTO.setSmonitorCutPayment(3*(Double.valueOf(df.format(percentSupplier/100))));//扣款人天
                    }
                    slaMonitorInterfaceDTO.setMonitorCutPayment(3);
                    slaMonitorInterfaceDTO.setMonitorDeductScore(15);//扣分
                } else {
                    slaMonitorInterfaceDTO.setMonitorCutPayment(0);//扣款人天
                    slaMonitorInterfaceDTO.setMonitorDeductScore(0);//扣分
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return slaMonitorInterfaceDTO;
    }
    /**
     *  //根据应用简称almShortName、fromDate 开始时间,toDate 结束时间得到已经关闭的任务列表
     * @param almShortName
     * @param fromDate
     * @param toDate
     * @return
     */
    @Override
    public List<StoryInfo> getClosedStoryList(String almShortName, Date fromDate, Date toDate,int pageNo,int pageSize) {
        List<StoryInfo> storyLists=new ArrayList<StoryInfo>();
       /* try {
            if(almShortName!=null&&fromDate!=null&&toDate!=null&&
                    !"".equals(almShortName)){
                storyLists = metricJiraService.getStoryList(almShortName,fromDate,toDate);
            }else{
                storyLists=null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            storyLists=null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            storyLists=null;
        }*/
        StoryInfo info=new StoryInfo();
        info.setAssigneeId("1");
        info.setAssigneeName("张三");
        info.setEpicTitle("sla接口调试");
        storyLists.add(info);
        return storyLists;
    }

    /**
     * 获取奖励考核数据
     * @param almApplicationSCode
     * @param fromDate
     * @param toDate
     * @param supplierId
     * @return
     */
    @Override
    public SlaBonuseInterfaceDTO getBonuseInfo(String almApplicationSCode, String fromDate, String toDate, String supplierId) {
        Map map=new HashMap();
        map.put("almApplicationSCode",almApplicationSCode);
        map.put("fromDate",fromDate);
        map.put("toDate",toDate);
        List<SlaBonusesDTO> list=slaBonusesDAO.getBonuseInfo(map);
        //只适应ALM的webservice
        ArrayList<SlaBonusesDTO> array = new ArrayList<SlaBonusesDTO>();
        for (SlaBonusesDTO dto : list) {
            array.add(dto);
        }
        SlaBonuseInterfaceDTO dto=new SlaBonuseInterfaceDTO();
        dto.setList(array);
        SlaBonusesDTO sdto=slaBonusesDAO.getSumScoreAndMoney(map);
        dto.setBonusesTotalMoney(sdto.getBonuseMoney());
        dto.setBonusesTotalScore(sdto.getBonuseScore());
        dto.setBonusesTotalNum(list.size());
        map.put("supplierId",supplierId);
        list=slaBonusesDAO.getBonuseInfo(map);
        sdto=slaBonusesDAO.getSumScoreAndMoney(map);
        dto.setPerbonusesTotalMoney(sdto.getBonuseMoney());
        dto.setPerbonusesTotalNum(list.size());
        dto.setPerbonusesTotalScore(sdto.getBonuseScore());
        dto.setAlmApplicationSCode(almApplicationSCode);
        return dto;
    }
}
