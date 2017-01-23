package com.haier.adp.sla.service;


import com.haier.adp.sla.dao.*;
import com.haier.adp.sla.dto.*;
import com.haier.adp.sla.util.Ideone;
import com.haier.adp.sla.util.TargetDataSource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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

    private SlaProjectInfoDAO slaProjectInfoDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 从paas平台取的数据 不指定数据源 默认
     * @param map
     * @return
     */
    @TargetDataSource(name="ds1")
    public List<PaasOutageDTO> getPaasOutageList(final Map map) {
        String sql = getSql(map);
        return (List<PaasOutageDTO>) jdbcTemplate.query(sql,new RowMapper<PaasOutageDTO>(){
            @Override
            public PaasOutageDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                PaasOutageDTO paasOutageDTO = new PaasOutageDTO();
                //type 0获取服务信息（实例均宕机）  1根据服务名称查询所有（服务）实例的宕机开始结束时间
                if(Integer.parseInt(map.get("type").toString())==0){
                    paasOutageDTO.setAppId(rs.getString("app_id"));
                    paasOutageDTO.setAppName(rs.getString("app_name"));
                    paasOutageDTO.setServiceName(rs.getString("service_name"));
                }else{
                    paasOutageDTO.setDowntimeBegin(rs.getTimestamp("downtime_begin"));
                    paasOutageDTO.setDowntimeEnd(rs.getTimestamp("downtime_end"));
                }
                return paasOutageDTO;
            }

        });
    }

    /**
     *获取总的宕机时间
     * @return map
     */
    @Override
    public long getSumOutageTime(Map map) {
        return slaOutageDAO.getSumOutageTime(map);
    }

    /**
     * 根据项目名称获取alm s码 应用简称
     * @param map 包含 projectName 项目名称
     * @return
     */
    @Override
    public List<SlaProjectInfoDTO> getSlaProjectInfoList(Map map) {
        return slaProjectInfoDAO.getSlaProjectInfoList(map);
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
        SlaOutageDTO dto=slaOutageDAO.getSlaOutage(map);
        String supplierList="";
        map.put("projectName",dto.getProjectName());
        //查供应商信息
        List<SlaSupplierDTO> slalist=slaOutageDAO.getSupplierByOutage(map);
       // dto.setSupplierList(JSONArray.fromObject(slalist).toString());
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


    /**
     * paas宕机信息查询sql
     * @param map
     * @return
     */
    public String getSql(Map map) {
        //1.先根据服务名称和宕机状态分组 得到服务名、宕机状态、数量
        //2.再根据服务名称分组  得到 服务名称、数量
        //3.取得只有一种宕机状态的服务
        //4. SELECT * FROM death_note WHERE service_name in （只有一种宕机状态的服务） AND health=1   取得宕机状态为宕机的服务信息
        String sql="";
        if(Integer.parseInt(map.get("type").toString())==0){
           sql=sql+" SELECT n.app_id,n.app_name,n.service_name,COUNT(1) FROM (";
        }else{
            sql=sql+"SELECT n.downtime_begin,n.downtime_end FROM (";
        }
        sql=sql+" SELECT * FROM death_note WHERE service_name "+
                "IN "+
        "(SELECT m.service_name FROM ("+
               " SELECT t.service_name,COUNT(1) num FROM (SELECT service_name,health,COUNT(1) AS num "+
                " FROM death_note where date='"+map.get("time")+"' "+
                " GROUP BY service_name,health) t GROUP BY t.service_name ) m WHERE "+
                " m.num=1) AND health=1 ORDER BY service_name ";
        if(Integer.parseInt(map.get("type").toString())==0){
            sql=sql+" ) n GROUP BY n.app_id,n.app_name,n.service_name";
        }else{
            sql=sql+" ) n WHERE n.service_name='"+map.get("serviceName")+"'";
        }
        return sql;
    }

    /**
     * ，向系统宕机表插入数据
     * @param slaOutageDTO
     */
    @Override
    public void insertSlaOutagelData(SlaOutageDTO slaOutageDTO) {
        slaOutageDAO.insertSlaOutagelData(slaOutageDTO);
    }

    /**
     *宕机确认画面 确认
     *
     */
    @Override
    public void updateSlaOutageData(Map map) {
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
                dto.setSupplier(obj.getString("supplier"));
                dto.setSupplierId(obj.getString("supplierId"));
                dto.setTSlaOutageId(slaOutageDTO.getId());
                dto.setType("1");
                dto.setCreateTime(time);
                slaOutageSupplierRelationDAO.insertOutageSupplierRelation(dto);
            }
        }
        //更新宕机信息表
        slaOutageDAO.updateSlaOutageData(slaOutageDTO);

    }

    @Override
    public void updateSlaListId(Map map){
        SlaOutageDTO slaOutageDTO = new SlaOutageDTO();
        slaOutageDTO.setAlmShortName(map.get("projectName").toString());
        String startString = map.get("startDate").toString() + " 00:00:00";
        String endString = map.get("endDate").toString() + " 23:59:59";
        slaOutageDTO.setQueryStartDate(Timestamp.valueOf(startString));
        slaOutageDTO.setQueryEndDate(Timestamp.valueOf(endString));
        slaOutageDTO.setTSlaListId(map.get("listId").toString());
        slaOutageDAO.updateSlaListId(slaOutageDTO);
    }

    /**
     * 宕机确认画面 查询
     * @param map 包含 projectName 项目名称,fromDate 宕机开始时间,toDate 宕机结束时间 pageNo 第几页,pageSize 每页多少条
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
        slaOutageList=slaOutageDAO.getSlaOutageList((map));
        if(slaOutageList.size()>0){
            for (int i = 0; i <slaOutageList.size() ; i++) {
                String supplierList="";
                SlaOutageDTO dto= slaOutageList.get(i);
                //查供应商信息
                Map newMap=new HashMap();
                newMap.put("id",dto.getId());
                newMap.put("projectName",dto.getProjectName());
                List<SlaSupplierDTO> slalist=slaOutageDAO.getSupplierByOutage(newMap);
                dto.setSupplierList(JSONArray.fromObject(slalist));
                System.out.println(supplierList);
            }
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
        slaOutageList=slaOutageDAO.getSlaOutageList((map));
        return slaOutageList.size();
    }

    /**
     * 查询供应商信息
     * @return
     */
    @Override
    public List<SlaSupplierDTO> getSlaSupplierList(Map map) {
        List<SlaSupplierDTO> slaSupplierList=new ArrayList<SlaSupplierDTO>();
        slaSupplierList=slaSupplierDAO.getSlaSupplierList(map);
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
        slaOutageList=slaOutageDAO.getSlaOutageList((map));
        SlaOutageInterfaceDTO slaOutageInterfaceDTO=new SlaOutageInterfaceDTO();
        slaOutageInterfaceDTO.setList(slaOutageList);
        //此处计算考核周期宕机时间 ≧ 24h * (考核周期 / 年度12个月)
        long time=getTotalTime(getSlaOutageList(map))/1000/60;
        long standard_time=24*(time/(12*30*24*60))*60;
        long num=getSumOutageTime(map);
        if(num>=standard_time){
            slaOutageInterfaceDTO.setAnnualCutPayment(25);
            slaOutageInterfaceDTO.setAnnualDeductScore(5);
        }else{
            slaOutageInterfaceDTO.setAnnualCutPayment(0);
            slaOutageInterfaceDTO.setAnnualDeductScore(0);
        }
       /* int totalTime = getTotalTime(slaOutageList);

        slaOutageInterfaceDTO.setTotalTime(totalTime);//宕机时间
        long time=(Timestamp.valueOf(map.get("toDate").toString()).getTime()-Timestamp.valueOf(map.get("fromDate").toString()).getTime());
        double percent=1-(long)totalTime/time;
        slaOutageInterfaceDTO.setPercent(percent*100+"%");//宕机稳定性
        slaOutageInterfaceDTO.setTotalNum(slaOutageList.size());//宕机次数
        //总的扣款人天
        slaOutageInterfaceDTO.setTotalCutPayment((int) slaOutageDAO.getTotalCutPayment(map));*/
        return slaOutageInterfaceDTO;
    }


}
