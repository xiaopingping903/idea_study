package com.haier.adp.sla.service;


import com.haier.adp.sla.dao.*;
import com.haier.adp.sla.dto.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/12/22.
 */
@Service(value="slaMonitorService")
@Slf4j
public class SlaMonitorServiceImpl implements SlaMonitorService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SlaMonitorDAO slaMonitorDAO;
    @Autowired
    private SlaOutageSupplierRelationDAO slaOutageSupplierRelationDAO;
    @Autowired
    private SlaOutageOperationDetailsDAO slaOutageOperationDetailsDAO;
    @Autowired
    private SlaProjectUserInfoDAO slaProjectUserInfoDAO;
    @Autowired
    private SlaSupplierDAO slaSupplierDAO;
    /**
     * 获取dubbo异常信息列表
     * @param map 包含almApplicationId S码，fromDate 宕机开始时间,toDate 宕机结束时间 pageNo 第几页,pageSize 每页多少条 tSlaListId tSlaListId主键
     * @return
     */
    @Override
    public List<SlaMonitorDTO> getSlaMonitorList(Map map) {
        if(map.get("pageNo")!=null&&map.get("pageSize")!=null){
            int pageNo=Integer.parseInt(map.get("pageNo").toString());
            int pageSize=Integer.parseInt(map.get("pageSize").toString());
            int startNo=pageNo-1;
            map.put("startNo",startNo);
        }
        List<SlaMonitorDTO> slaMonitorList=new ArrayList<SlaMonitorDTO>();
        try{
            slaMonitorList=slaMonitorDAO.getSlaMonitorList((map));
            String supplierList="";
            if(slaMonitorList.size()>0){
                for (int i = 0; i <slaMonitorList.size() ; i++) {
                    SlaMonitorDTO dto= slaMonitorList.get(i);
                    //查供应商信息
                    Map newMap=new HashMap();
                    newMap.put("id",dto.getId());
                    newMap.put("projectName",dto.getAlmShortName());
                    List<SlaSupplierDTO> slalist=slaMonitorDAO.getSupplierByMonitor(newMap);
                    dto.setSupplierList(JSONArray.fromObject(slalist));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return slaMonitorList;
    }

    @Override
    public void updateSlaListId(Map map) {
        SlaMonitorDTO dto = new SlaMonitorDTO();
        dto.setAlmShortName(map.get("projectName").toString());
        dto.setTSlaListId(Long.valueOf(map.get("listId").toString()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(map.get("startDate").toString());
            Date endDate = sdf.parse(map.get("endDate").toString());
            dto.setQueryStartDate(startDate);
            dto.setQueryEndDate(endDate);
            slaMonitorDAO.updateSlaListId(dto);
        } catch (ParseException e) {
            log.error("传入数据格式错误.{}", map.toString());
        }
    }
    /**
     * 查询dubbo异常信息总数
     * @param map 包含projectName 项目名称，fromDate 宕机开始时间,toDate 宕机结束时间 pageNo 第几页,pageSize 每页多少条 tSlaListId tSlaListId主键
     * @return
     */
    @Override
    public int querySlaMonitorListCount(Map map) {
        map.put("pageNo",null);
        map.put("pageSize",null);
        int count=0;
        try{
            count = slaMonitorDAO.getSlaMonitorList(map).size();
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return count;
    }

    /**
     * 计算dubbo异常调用比例
     * @param map
     * @return
     */
    @Override
    public SlaMonitorDTO getPercent(Map map) {
        SlaMonitorDTO dto=new SlaMonitorDTO();
        try{
            dto=slaMonitorDAO.getPercent(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return dto;
    }

    /**
     * 更新dubbo异常信息
     * @param map
     */
    @Override
    public void updateSlaMonitorData(Map map) {
        try{
            //根据id查询宕机信息
            Map mapResult=new HashMap();
            mapResult.put("id",map.get("id"));
            SlaMonitorDTO slaMonitorDTO=slaMonitorDAO.getSlaMonitor(mapResult);
            SlaOutageOperationDetailsDTO slaOutageOperationDetailsDTO=new SlaOutageOperationDetailsDTO();
            Timestamp time=new java.sql.Timestamp(new java.util.Date().getTime());
            String operator=map.get("operator")+"";
            String content=operator+"在"+time;
            String operatorId=map.get("operatorId")+"";
            if(!"".equals(operatorId)&&null!=operatorId&&!"null".equals(operatorId)){
                slaOutageOperationDetailsDTO.setOperatorId(operatorId);
                slaMonitorDTO.setOperatorId(operatorId);
            }
            if(!"".equals(operator)&&null!=operator&&!"null".equals(operator)){
                slaOutageOperationDetailsDTO.setOperator(operator);
                slaMonitorDTO.setOperator(operator);
            }
            slaMonitorDTO.setOperatorTime(time);
            int n=0;
            if(map.get("assessStatus")!=null&&!slaMonitorDTO.getAssessStatus().equals(map.get("assessStatus"))){
                content=content+"将考核状态由"+slaMonitorDTO.getAssessStatus()+"改为"+map.get("assessStatus")+";";
                slaMonitorDTO.setAssessStatus(map.get("assessStatus")+"");
                n=n+1;
            }
     /*   if(!slaOutageDTO.getSupplierId().equals(jsonObject.getString("supplierId"))){
            content=content+"将供应商id由"+slaOutageDTO.getSupplierId()+"改为"+jsonObject.getString("supplierId")
                    +",将供应商名称由"+slaOutageDTO.getSupplier()+"改为"+jsonObject.getString("supplier")+";";
            slaOutageDTO.setSupplierId(jsonObject.getString("supplierId"));
            slaOutageDTO.setSupplier(jsonObject.getString("supplier"));
            n=n+1;
        }*/
            if("0".equals(slaMonitorDTO.getStatus())) {
                content = content + "将处理状态由0改为1;";
                slaMonitorDTO.setStatus("1");
                n=n+1;
            }

            slaOutageOperationDetailsDTO.setCreateTime(time);
            slaOutageOperationDetailsDTO.setTSlaMonitorId(Integer.parseInt(map.get("id").toString()));
            if(((slaMonitorDTO.getCancelAssessReason()==null||"null".equals(slaMonitorDTO.getCancelAssessReason()))&&!"null".equals(map.get("cancelAssessReason"))&&
                    null!=map.get("cancelAssessReason")&&!"".equals(map.get("cancelAssessReason")))||
                    (slaMonitorDTO.getCancelAssessReason()!=null&&!"null".equals(slaMonitorDTO.getCancelAssessReason())
                            &&!slaMonitorDTO.getCancelAssessReason().equals(map.get("cancelAssessReason")))){
                content=content+"将取消考核理由由"+slaMonitorDTO.getCancelAssessReason()+"改为"+map.get("cancelAssessReason");
                slaMonitorDTO.setCancelAssessReason(map.get("cancelAssessReason")+"");
                n=n+1;
            }
            if(n==0){
                content=content+"未做任何改动";
            }
            slaOutageOperationDetailsDTO.setType("2");
            slaOutageOperationDetailsDTO.setContent(content);
            //向操作表插入一条数据；
            slaOutageOperationDetailsDAO.insertSlaOutageOperationDetailsData(slaOutageOperationDetailsDTO);
            //想供应商宕机信息关联表插入数据
            JSONArray jsonArray=new JSONArray();
            if(map.get("supplierList")!=null&&!"".equals(map.get("supplierList"))){
                jsonArray = JSONArray.fromObject(map.get("supplierList"));
            }
            //先删除关联该宕机的供应商表
            SlaOutageSupplierRelationDTO slaOutageSupplierRelationDTO=new SlaOutageSupplierRelationDTO();
            slaOutageSupplierRelationDTO.setTSlaMonitorId(slaMonitorDTO.getId());
            slaOutageSupplierRelationDTO.setType("2");
            slaOutageSupplierRelationDAO.deleteOutageSupplierRelation(slaOutageSupplierRelationDTO);
            if(jsonArray.size()>0){
                for (int i = 0; i <jsonArray.size() ; i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    SlaOutageSupplierRelationDTO dto = new SlaOutageSupplierRelationDTO();
                    Map mm=new HashMap();
                    mm.put("supplierId",obj.getString("supplierId"));
                    List<SlaSupplierDTO> slaSupplierList=slaSupplierDAO.getSlaSupplierList(mm);
                    if(slaSupplierList.size()>0) {
                        dto.setSupplier(obj.getString("supplier"));
                        dto.setSupplierId(obj.getString("supplierId"));
                        dto.setTSlaMonitorId(slaMonitorDTO.getId());
                        dto.setType("2");
                        dto.setCreateTime(time);
                        slaOutageSupplierRelationDAO.insertOutageSupplierRelation(dto);
                    }
                }
            }
            //更新宕机信息表
            slaMonitorDAO.updateMonitorData(slaMonitorDTO);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
    }

    /**
     * 插入
     * @param slaMonitorDTO
     */
    @Override
    public void insertSlaMonitor(SlaMonitorDTO slaMonitorDTO) {
        try{
            slaMonitorDAO.insert(slaMonitorDTO);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
    }

    @Override
    public String updateSupplierPercent(Map map) {
        String ifSuccess="1";

        try{
            JSONArray array=JSONArray.fromObject(map.get("supplierList"));
            if(array.size()>0){
                int totalpercent=0;
                for (int i = 0; i <array.size() ; i++) {
                    JSONObject jsonObj=array.getJSONObject(i);
                    int percent=Integer.parseInt(jsonObj.get("percent")+"");
                    totalpercent=totalpercent+percent;
                }
                if(totalpercent==100||totalpercent==0){
                    for (int i = 0; i <array.size() ; i++) {
                        JSONObject jsonObj=array.getJSONObject(i);
                        Map mm=new HashMap();
                        mm.put("projectName",map.get("projectName"));
                        mm.put("supplierId",jsonObj.get("supplierId"));
                        int percent=Integer.parseInt(jsonObj.get("percent")+"");
                        mm.put("percent",percent);
                        slaMonitorDAO.updateSupplierPercent(mm);
                    }
                }else{
                    ifSuccess="0";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            ifSuccess="0";
            logger.info("数据访问资源失败");
        }
        return ifSuccess;
    }

    /**
     *根据项目经理获得对应的dubbo信息
     * @param mmm
     * @return
     */
    @Override
    public List<SlaMonitorDTO> getServiceNum(Map mmm) {
        List<SlaMonitorDTO> list=new ArrayList<SlaMonitorDTO>();
        try{
            list=slaMonitorDAO.getServiceNum(mmm);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
       }
        return list;
    }
    /**
     *获得应用对应的项目经理
     * @param map
     * @return
     */
    @Override
    public List<SlaProjectUserInfoDTO> getPMMonitorList(Map map) {
        List<SlaProjectUserInfoDTO> list=new ArrayList<SlaProjectUserInfoDTO>();
        try{
            list=slaProjectUserInfoDAO.getPMMonitorList(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return list;
    }


    /**
     *   @param  almShortName 应用简称 tSlaListId sla记录主键 supplierId供应商id
     *根据s码和时间段查宕机信息
     * @return
     */
    @Override
    public SlaMonitorInterfaceDTO getMointorInfo(String almShortName,int tSlaListId) {
        Map map = new HashMap();
        map.put("almShortName",almShortName);
        map.put("tSlaListId",tSlaListId);
        SlaMonitorInterfaceDTO slaMonitorInterfaceDTO = new SlaMonitorInterfaceDTO();
        try {
            SlaMonitorDTO slaMonitorDTO = slaMonitorDAO.getPercent(map);
            if (slaMonitorDTO != null) {
                slaMonitorInterfaceDTO.setInvokedTotal(new Double(slaMonitorDTO.getInvokedTotal()).intValue());//总次数
                slaMonitorInterfaceDTO.setInvokedSuccessTotal(new Double(slaMonitorDTO.getInvokedSuccessTotal()).intValue());//成功次数
                slaMonitorInterfaceDTO.setInvokedFailTotal(new Double(slaMonitorDTO.getInvokedFailTotal()).intValue());//失败次数
                float percent = (float) slaMonitorDTO.getInvokedFailTotal() / (float) slaMonitorDTO.getInvokedTotal();
                DecimalFormat df = new DecimalFormat("##.##");
                slaMonitorInterfaceDTO.setPercent(df.format(percent * 100) + "%");//稳定性
                if (percent >= 0.01) {
                    slaMonitorInterfaceDTO.setMonitorCutPayment(3);//扣款人天
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
}
