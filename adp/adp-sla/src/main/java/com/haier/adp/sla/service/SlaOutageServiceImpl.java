package com.haier.adp.sla.service;


import com.haier.adp.common.constants.Constants;
import com.haier.adp.sla.dao.*;
import com.haier.adp.sla.dto.*;
import com.haier.adp.sla.util.Ideone;
import com.haier.adp.sla.util.TargetDataSource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */
@Service(value="slaOutageService")
public class SlaOutageServiceImpl implements SlaOutageService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SlaOutageDAO slaOutageDAO;
    @Autowired
    private SlaOutageOperationDetailsDAO slaOutageOperationDetailsDAO;
    @Autowired
    private PaasOutageDAO paasOutageDAO;
    @Autowired
    private SlaSupplierDAO slaSupplierDAO;
    @Autowired
    private SlaOutageSupplierRelationDAO slaOutageSupplierRelationDAO;
    @Autowired
    private SlaProjectInfoDAO slaProjectInfoDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SlaProjectUserInfoDAO slaProjectUserInfoDAO;
    /**
     * 从paas平台取的数据 不指定数据源 默认
     * @param map
     * @return
     */
    @TargetDataSource(name="ds1")
    public List<PaasOutageDTO> getPaasOutageList(final Map map){
        String sql=getOutageSql(map);
        List<PaasOutageDTO> paasOutageDTOList=new ArrayList<PaasOutageDTO>();
        try{
            paasOutageDTOList = (List<PaasOutageDTO>) jdbcTemplate.query(sql,new RowMapper<PaasOutageDTO>(){
                @Override
                public PaasOutageDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    PaasOutageDTO paasOutageDTO = new PaasOutageDTO();
                    //type 0获取服务信息（实例均宕机）  1根据服务名称查询所有（服务）实例的宕机开始结束时间
                    if(map.get("type").equals("0")){
                        paasOutageDTO.setAppId(rs.getString("app_id"));
                        paasOutageDTO.setAppName(rs.getString("app_name"));
                        paasOutageDTO.setServiceName(rs.getString("service_name"));
                    }else if(map.get("type").equals("2")){
                        paasOutageDTO.setDowntimeBegin(rs.getTimestamp("downtime_begin"));
                        paasOutageDTO.setDowntimeEnd(rs.getTimestamp("downtime_end"));
                    }else{
                        paasOutageDTO.setContainerId(rs.getString("container_id"));
                    }
                    return paasOutageDTO;
                }

            });
        }catch (Exception e){
            e.printStackTrace();
            logger.info("paas数据库连接异常");
        }
        return paasOutageDTOList;
    }

    /**
     *获取总的宕机时间
     * @return map
     */
    @Override
    public long getSumOutageTime(Map map) {
        long timeNum=0;
        try{
            timeNum=slaOutageDAO.getSumOutageTime(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return timeNum;
    }

    /**
     * 根据项目名称获取alm s码 应用简称
     * @param map 包含 projectName 项目名称
     * @return
     */
    @Override
    public List<SlaProjectInfoDTO> getSlaProjectInfoList(Map map) {
        List<SlaProjectInfoDTO> list=new ArrayList<SlaProjectInfoDTO>();
        try{
            list=slaProjectInfoDAO.getSlaProjectInfoList(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return list;
    }


    /**
     * 根据id获取宕机信息
     * @param id
     * @return
     */
    @Override
    public SlaOutageDTO getSlaOutage(int id) {
        Map map=new HashMap();
        map.put("id",id);
        SlaOutageDTO dto=new SlaOutageDTO();
        try{
            dto = slaOutageDAO.getSlaOutage(map);
            String supplierList="";
            map.put("projectName",dto.getProjectName());
            //查供应商信息
            List<SlaSupplierDTO> slalist=slaOutageDAO.getSupplierByOutage(map);
            // dto.setSupplierList(JSONArray.fromObject(slalist).toString());
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }

        return dto;
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



    public String getOutageSql(Map map){
        String sql="";
        //查所有实例均为宕机的服务
        if(map.get("type").equals("0")){
            sql="SELECT m.app_id,m.app_name,m.service_name FROM (\n" +
                    "SELECT m.app_id,m.app_name,m.service_name,COUNT(1) AS num FROM (\n" +
                    "SELECT  app_id,app_name,service_name,health,COUNT(1) AS num FROM death_note WHERE DATE='"+map.get("time")+"' GROUP BY app_id,app_name,service_name,health\n" +
                    ")m \n" +
                    "GROUP BY m.app_id,m.app_name,m.service_name) m WHERE m.num=1\n";
        }
        //查服务下的所有实例
        if(map.get("type").equals("1")){
            sql="SELECT  DISTINCT container_id FROM death_note WHERE DATE='"+map.get("time")+"' AND service_name='"+map.get("serviceName")+"'";
        }
        if(map.get("type").equals("2")){
            sql="SELECT  downtime_begin,downtime_end FROM death_note WHERE DATE='"+map.get("time")+"' AND service_name='"+map.get("serviceName")+"' AND container_id='"+map.get("containerId")+"'";
        }
        return sql;
    }
    /**
     * ，向系统宕机表插入数据
     * @param slaOutageDTO
     */
    @Override
    public void insertSlaOutagelData(SlaOutageDTO slaOutageDTO) {
        try{
            slaOutageDAO.insertSlaOutagelData(slaOutageDTO);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
    }

    /**
     *宕机确认画面 确认
     *
     */
    @Override
    public void updateSlaOutageData(Map map) {
        try{
            //根据id查询宕机信息
            Map mapResult=new HashMap();
            mapResult.put("id",map.get("id"));
            SlaOutageDTO slaOutageDTO=slaOutageDAO.getSlaOutage(mapResult);
            SlaOutageOperationDetailsDTO slaOutageOperationDetailsDTO=new SlaOutageOperationDetailsDTO();
            Timestamp time=new java.sql.Timestamp(new java.util.Date().getTime());
            String operator=map.get("operator")+"";
            String content=operator+"在"+time;
            String operatorId=map.get("operatorId")+"";
            if(!"".equals(operatorId)&&null!=operatorId&&!"null".equals(operatorId)){
                slaOutageOperationDetailsDTO.setOperatorId(operatorId);
                slaOutageDTO.setOperatorId(operatorId);
            }
            if(!"".equals(operator)&&null!=operator&&!"null".equals(operator)){
                slaOutageOperationDetailsDTO.setOperator(operator);
                slaOutageDTO.setOperator(operator);
            }
            slaOutageDTO.setOutageConfirmDate(time);
            int n=0;
            if(map.get("assessStatus")!=null&&!slaOutageDTO.getAssessStatus().equals(map.get("assessStatus"))){
                content=content+"将考核状态由"+slaOutageDTO.getAssessStatus()+"改为"+map.get("assessStatus")+";";
                slaOutageDTO.setAssessStatus(map.get("assessStatus")+"");
                n=n+1;
            }
     /*   if(!slaOutageDTO.getSupplierId().equals(jsonObject.getString("supplierId"))){
            content=content+"将供应商id由"+slaOutageDTO.getSupplierId()+"改为"+jsonObject.getString("supplierId")
                    +",将供应商名称由"+slaOutageDTO.getSupplier()+"改为"+jsonObject.getString("supplier")+";";
            slaOutageDTO.setSupplierId(jsonObject.getString("supplierId"));
            slaOutageDTO.setSupplier(jsonObject.getString("supplier"));
            n=n+1;
        }*/
            if(map.get("ifNotRun")!=null&&!slaOutageDTO.getIfNotRun().equals(map.get("ifNotRun"))){
                content=content+"将是否业务无法执行造成严重损失由"+slaOutageDTO.getIfNotRun()+"改为"+map.get("ifNotRun")+";";
                slaOutageDTO.setIfNotRun(map.get("ifNotRun")+"");
                if("1".equals(map.get("ifNotRun"))){
                    content=content+"将扣款由"+slaOutageDTO.getCutPayment()+"经理人天改为"+slaOutageDTO.getCutPayment()+5+"经理人天";
                    slaOutageDTO.setCutPayment(slaOutageDTO.getCutPayment()+5);
                }else{
                    content=content+"将扣款由"+slaOutageDTO.getCutPayment()+"经理人天改为"+(slaOutageDTO.getCutPayment()-5)+"经理人天";
                    slaOutageDTO.setCutPayment(slaOutageDTO.getCutPayment()-5);
                }
                n=n+1;
            }
            if("0".equals(slaOutageDTO.getStatus())) {
                content = content + "将处理状态由0改为1;";
                slaOutageDTO.setStatus("1");
                n=n+1;
            }
            slaOutageOperationDetailsDTO.setCreateTime(time);
            slaOutageOperationDetailsDTO.setType("1");
            slaOutageOperationDetailsDTO.setTSlaOutageId(Integer.parseInt(map.get("id").toString()));
            if(((slaOutageDTO.getCancelAssessReason()==null||"null".equals(slaOutageDTO.getCancelAssessReason()))&&!"null".equals(map.get("cancelAssessReason"))&&
                    null!=map.get("cancelAssessReason")&&!"".equals(map.get("cancelAssessReason")))||
                    (slaOutageDTO.getCancelAssessReason()!=null&&!"null".equals(slaOutageDTO.getCancelAssessReason())
                            &&!slaOutageDTO.getCancelAssessReason().equals(map.get("cancelAssessReason")))){
                slaOutageDTO.setCancelAssessReason(map.get("cancelAssessReason")+"");
                n=n+1;
            }
            if(n==0){
                content=content+"未做任何改动";
            }
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
            slaOutageSupplierRelationDTO.setTSlaOutageId(slaOutageDTO.getId());
            slaOutageSupplierRelationDTO.setType("1");
            slaOutageSupplierRelationDAO.deleteOutageSupplierRelation(slaOutageSupplierRelationDTO);
            if(jsonArray.size()>0){
                for (int i = 0; i <jsonArray.size() ; i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    SlaOutageSupplierRelationDTO dto = new SlaOutageSupplierRelationDTO();
                    Map mm=new HashMap();
                    mm.put("supplierId",obj.getString("supplierId"));
                    List<SlaSupplierDTO> slaSupplierList=slaSupplierDAO.getSlaSupplierList(mm);
                    if(slaSupplierList.size()>0){
                        dto.setSupplier(obj.getString("supplier"));
                        dto.setSupplierId(obj.getString("supplierId"));
                        dto.setTSlaOutageId(slaOutageDTO.getId());
                        dto.setType("1");
                        dto.setCreateTime(time);
                        slaOutageSupplierRelationDAO.insertOutageSupplierRelation(dto);
                    }
                }
            }
            //更新宕机信息表
            slaOutageDAO.updateSlaOutageData(slaOutageDTO);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
    }

    @Override
    public void updateSlaListId(Map map){
        try{
            SlaOutageDTO slaOutageDTO = new SlaOutageDTO();
            slaOutageDTO.setAlmShortName(map.get("projectName").toString());
            String startString = map.get("startDate").toString() + " 00:00:00";
            String endString = map.get("endDate").toString() + " 23:59:59";
            slaOutageDTO.setQueryStartDate(Timestamp.valueOf(startString));
            slaOutageDTO.setQueryEndDate(Timestamp.valueOf(endString));
            slaOutageDTO.setTSlaListId(map.get("listId").toString());
            slaOutageDAO.updateSlaListId(slaOutageDTO);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
    }

    /**
     * 宕机确认画面 查询
     * @param map 包含 s_code S码,projectName 应用简称名称,fromDate 宕机开始时间,toDate 宕机结束时间 pageNo 第几页,pageSize 每页多少条
     * @return
     */
    @Override
    public List<SlaOutageDTO> getSlaOutageList(Map map) {
        if(map.get("pageNo")!=null&&map.get("pageSize")!=null){
            int pageNo=Integer.parseInt(map.get("pageNo").toString());
            int pageSize=Integer.parseInt(map.get("pageSize").toString());
            int startNo=pageNo-1;
            map.put("startNo",startNo);
        }
        List<SlaOutageDTO> slaOutageList=new ArrayList<SlaOutageDTO>();
        try{
            slaOutageList=slaOutageDAO.getSlaOutageList((map));
            if(slaOutageList.size()>0){
                for (int i = 0; i <slaOutageList.size() ; i++) {
                    String supplierList="";
                    SlaOutageDTO dto= slaOutageList.get(i);
                    //查供应商信息
                    Map newMap=new HashMap();
                    newMap.put("tSlaOutageId",dto.getId());
                    newMap.put("projectName",dto.getAlmShortName());
                    List<SlaSupplierDTO> slalist=slaOutageDAO.getSupplierByOutage(newMap);
                    dto.setSupplierList(JSONArray.fromObject(slalist));
                    System.out.println(supplierList);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return slaOutageList;
    }

    /**
     *查询宕机信息总条数
     * @param map 包含projectId,startTime,endTime pageNo,pageSize
     * @return
     */
    @Override
    public int querySlaOutageListCount(Map map) {
        map.put("pageSize",null);
        map.put("startNo",null);
        List<SlaOutageDTO> slaOutageList=new ArrayList<SlaOutageDTO>();
        try{
            slaOutageList=slaOutageDAO.getSlaOutageList((map));
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return slaOutageList.size();
    }

    /**
     * 查询供应商信息
     * @return
     */
    @Override
    public List<SlaSupplierDTO> getSlaSupplierList(Map map) {
        List<SlaSupplierDTO> slaSupplierList=new ArrayList<SlaSupplierDTO>();
        try{
            slaSupplierList=slaSupplierDAO.getSlaSupplierList(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return slaSupplierList;
    }
    /**
     *   @param  almShortName 应用简称 tSlaListId sla记录主键
     *根据s码和时间段查宕机信息
     * @return
     */
    @Override
    public SlaOutageInterfaceDTO getOutageInfo(String almShortName,int tSlaListId,String fromDate,String toDate) {
        Map map=new HashMap();
        map.put("almShortName",almShortName);
        map.put("tSlaListId",tSlaListId);
        map.put("fromDate",fromDate);
        map.put("toDate",toDate);
        List<SlaOutageDTO> slaOutageList=new ArrayList<SlaOutageDTO>();
        //type==1 sla详情页只查考核的
        map.put("type","1");
        SlaOutageInterfaceDTO slaOutageInterfaceDTO=new SlaOutageInterfaceDTO();
        try {
            slaOutageList = slaOutageDAO.getSlaOutageList((map));
            if (slaOutageList.size() > 0) {
                slaOutageInterfaceDTO.setList(slaOutageList);
                //此处计算考核周期宕机时间 ≧ 24h * (考核周期 / 年度12个月)
                float time = (float) (Timestamp.valueOf(map.get("toDate").toString()).getTime() - Timestamp.valueOf(map.get("fromDate").toString()).getTime()) / 1000 / 60;
                float standard_time = 36 * (time / (12 * 30 * 24 * 60)) * 60;
                float num = (float) getTotalTime(getSlaOutageList(map)) / 1000 / 60;
                if (num >= standard_time) {
                    slaOutageInterfaceDTO.setAnnualCutPayment(25);
                    slaOutageInterfaceDTO.setAnnualDeductScore(5);
                } else {
                    slaOutageInterfaceDTO.setAnnualCutPayment(0);
                    slaOutageInterfaceDTO.setAnnualDeductScore(0);
                }
             }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return slaOutageInterfaceDTO;
    }

    /**
     *根据项目经理获得对应的宕机信息
     * @param map
     * @return
     */
    public List<SlaOutageDTO> getServiceNum(Map map){
        List<SlaOutageDTO> list=new ArrayList<SlaOutageDTO>();
        try{
            list=slaOutageDAO.getServiceNum(map);
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
    public List<SlaProjectUserInfoDTO> getPMList(Map map){
        List<SlaProjectUserInfoDTO> list=new ArrayList<SlaProjectUserInfoDTO>();
        try{
               list=slaProjectUserInfoDAO.getPMList(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return list;
    }

    @Override
    public Map getRole(String adpAccountId) {
        Map resultMap=new HashMap();
        Map map=new HashMap();
        map.put("account",adpAccountId);
        List<SlaProjectUserInfoDTO> dtolist=slaProjectUserInfoDAO.getUserInfo(map);
        String type="";
        List listName=new ArrayList();
        if(dtolist.size()>0){
            for (int i = 0; i <dtolist.size() ; i++) {
                String role=dtolist.get(i).getRole();
                if(role.indexOf(Constants.SYS_ROLES.SYSTEM_ADMIN)==-1&&role.indexOf(Constants.SYS_ROLES.ORG_ADMIN)==-1){
                    //不是组织管理者
                    type="0";
                    if(role.indexOf(Constants.SYS_ROLES.PROJECT_MANAGER)!=-1){
                        //是项目经理
                        Map mm=new HashMap();
                        mm.put("id",dtolist.get(i).getTSlaProjectInfoId());
                        List<SlaProjectInfoDTO> infolist=slaProjectInfoDAO.getSlaProjectInfoList(mm);
                        if(infolist.size()>0){
                            listName.add(infolist.get(0).getProjectName());
                        }
                    }
                }else{
                    //是组织管理者，跳出循环
                    type="1";
                    break;
                }
            }
        }
        resultMap.put("type",type);
        resultMap.put("listName",listName);
        return resultMap;
    }

    @Override
    public int getNotSetSupplierOutageNum(Map map) {
        return slaOutageDAO.getNotSetSupplierOutageNum(map);
    }
}
