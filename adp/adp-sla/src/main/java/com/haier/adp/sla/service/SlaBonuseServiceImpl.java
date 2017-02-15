package com.haier.adp.sla.service;


import com.haier.adp.common.constants.Constants;
import com.haier.adp.sla.dao.*;
import com.haier.adp.sla.dto.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/12/22.
 */
@Service(value="slaBonuseService")
public class SlaBonuseServiceImpl implements SlaBonuseService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SlaBonusesDAO slaBonusesDAO;
    @Autowired
    private SlaBonusesDetailDAO slaBonusesDetailDAO;
    @Autowired
    private SlaBonusesTypeDAO slaBonusesTypeDAO;
    @Autowired
    private SlaBonusesAttachmentDAO slaBonusesAttachmentDAO;
    @Autowired
    private SlaProjectUserInfoDAO slaProjectUserInfoDAO;
    @Autowired
    private SlaProjectInfoDAO slaProjectInfoDAO;
    @Override
    public List<SlaBonusesDTO> getSlaBonusesList(Map map) {
        if(map.get("pageNo")!=null&&map.get("pageSize")!=null){
            int pageNo=Integer.parseInt(map.get("pageNo").toString());
            int pageSize=Integer.parseInt(map.get("pageSize").toString());
            int startNo=pageNo-1;
            map.put("startNo",startNo);
        }
        List<SlaBonusesDTO> slaBonusesList=new ArrayList<SlaBonusesDTO>();
        try{
            slaBonusesList = slaBonusesDAO.getSlaBonusesList(map);
            if(slaBonusesList.size()>0){
                for (int i = 0; i <slaBonusesList.size() ; i++) {
                    SlaBonusesDTO dto= slaBonusesList.get(i);
                    Map mm=new HashMap();
                    mm.put("tSlaBonusesId",dto.getId());
                    List<SlaBonusesDetailDTO> slaBonusesDetailDTOList=slaBonusesDetailDAO.getBonuseItemList(mm);
                    if(slaBonusesDetailDTOList.size()>0){
                        for (int j = 0; j <slaBonusesDetailDTOList.size() ; j++) {
                            SlaBonusesDetailDTO detailDto=slaBonusesDetailDTOList.get(j);
                            Map mmm=new HashMap();
                            mmm.put("tSlaBonusesDetailId",detailDto.getDetailId());
                            List<SlaBonusesAttachmentDTO> fileList=slaBonusesAttachmentDAO.getBonuseAttachmentList(mmm);
                            detailDto.setFileList(fileList);
                        }
                    }
                    dto.setBonuseItemList(slaBonusesDetailDTOList);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return slaBonusesList;
    }

    @Override
    public List<SlaBonusesTypeDTO> getBonusesTypeList() {
        List<SlaBonusesTypeDTO> list=new ArrayList<SlaBonusesTypeDTO>();
        try{
         list=slaBonusesTypeDAO.getBonusesTypeList(null);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return list;
    }

    @Override
    public String saveAddSlaBonuse(Map map) {
        String ifSuccess="1";
        try{
            int id=0;
            SlaBonusesDTO slaBonusesDTO = new SlaBonusesDTO();
            if(map.get("type").equals("0")){
                slaBonusesDTO = new SlaBonusesDTO();
                slaBonusesDTO.setAlmApplicationId(map.get("almApplicationId")+"");
                slaBonusesDTO.setAlmShortName(map.get("almShortName")+"");
                slaBonusesDTO.setBonuseDate(new Date());
                slaBonusesDTO.setCreateTime(new Timestamp(new Date().getTime()));
                slaBonusesDTO.setSupplier(map.get("supplier")+"");
                slaBonusesDTO.setSupplierId(map.get("supplierId")+"");
                slaBonusesDTO.setIfDel("0");
                slaBonusesDAO.insertSlaBonusesDTO(slaBonusesDTO);
            }else{
                try{
                    slaBonusesDTO=slaBonusesDAO.getSlaBonusesList(map).get(0);
                }catch (Exception e){
                    e.printStackTrace();
                    ifSuccess="0";
                }
                //删除详情
                slaBonusesDetailDAO.deleteSlaBonusesDetail(map);
                //删除附件
                slaBonusesAttachmentDAO.deleteSlaBonusesAttachment(map);
            }
            slaBonusesDTO.setOperator(map.get("operator")+"");
            slaBonusesDTO.setOperatorId(map.get("operatorId")+"");
            if(ifSuccess.equals("1")){
                id=slaBonusesDTO.getId();
                int totalScore=0;
                double totalMoney=0;
                String bonuseType="";
                JSONArray jsonArray=new JSONArray();
                if(map.get("bonuseItemList")!=null&&!"".equals(map.get("bonuseItemList"))&&!"null".equals(map.get("bonuseItemList"))){
                    jsonArray = JSONArray.fromObject(map.get("bonuseItemList"));
                    if(jsonArray.size()>0){
                        for (int i = 0; i <jsonArray.size() ; i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            if("1".equals(jsonObject.get("selected"))){
                                Map mm=new HashMap();
                                mm.put("id",jsonObject.get("id"));
                                List<SlaBonusesTypeDTO> list=slaBonusesTypeDAO.getBonusesTypeList(mm);
                                SlaBonusesTypeDTO slaBonusesTypeDTO=list.get(0);
                                SlaBonusesDetailDTO slaBonusesDetailDTO=new SlaBonusesDetailDTO();
                                slaBonusesDetailDTO.setOperator(map.get("operator")+"");
                                slaBonusesDetailDTO.setOperatorId(map.get("operatorId")+"");
                                slaBonusesDetailDTO.setCreateTime(new Timestamp(new Date().getTime()));
                             /*   slaBonusesDetailDTO.setAttachment(jsonObject.get("attachment")+"");
                                slaBonusesDetailDTO.setUrl(jsonObject.get("url")+"");*/
                                slaBonusesDetailDTO.setBonuseContent(slaBonusesTypeDTO.getBonuseContent());
                                slaBonusesDetailDTO.setBonuseMoney(slaBonusesTypeDTO.getBonuseMoney());
                                slaBonusesDetailDTO.setBonuseScore(slaBonusesTypeDTO.getBonuseScore());
                                slaBonusesDetailDTO.setBonuseTitle(slaBonusesTypeDTO.getBonuseTitle());
                                slaBonusesDetailDTO.setTSlaBonusesId(id);
                                slaBonusesDetailDTO.setIfDel("0");
                                slaBonusesDetailDTO.setTSlaBonusesTypeId(slaBonusesTypeDTO.getId());
                                slaBonusesDetailDAO.insertSlaBonusesDetail(slaBonusesDetailDTO);
                                totalScore=totalScore+slaBonusesTypeDTO.getBonuseScore();
                                totalMoney=totalMoney+slaBonusesTypeDTO.getBonuseMoney();
                                bonuseType=bonuseType+slaBonusesTypeDTO.getBonuseTitle()+";";
                                int detailId=0;
                                detailId=slaBonusesDetailDTO.getId();
                                JSONArray jsonArrayAttachment=new JSONArray();
                                if(jsonObject.get("fileList")!=null&&!"".equals(jsonObject.get("fileList"))&&!"null".equals(jsonObject.get("fileList"))) {
                                    jsonArrayAttachment = JSONArray.fromObject(jsonObject.get("fileList"));
                                    if(jsonArrayAttachment.size()>0) {
                                        for (int j = 0; j < jsonArrayAttachment.size(); j++) {
                                            JSONObject obj=jsonArrayAttachment.getJSONObject(j);
                                            SlaBonusesAttachmentDTO sbad=new SlaBonusesAttachmentDTO();
                                            sbad.setAttachment(obj.get("attachment")+"");
                                            sbad.setUrl(obj.get("url")+"");
                                            sbad.setUid(obj.get("uid")+"");
                                            sbad.setTSlaBonusesDetailId(detailId);
                                            sbad.setCreateTime(new Timestamp(new Date().getTime()));
                                            slaBonusesAttachmentDAO.insertSlaBonusesAttachmentDTO(sbad);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                slaBonusesDTO.setBonuseType(bonuseType);
                slaBonusesDTO.setBonuseMoney(totalMoney);
                slaBonusesDTO.setBonuseScore(totalScore);
                slaBonusesDAO.updateSlaBonuses(slaBonusesDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return ifSuccess;
    }

    @Override
    public String delSlaBonuse(Map map) {
        String ifSuccess="1";
        try{
            int id=slaBonusesDAO.delSlaBonuse(map);
            if(id==0){
                ifSuccess="0";
            }else{
                slaBonusesDetailDAO.delSlaBonusesDetail(map);
                //删除附件
                slaBonusesAttachmentDAO.deleteSlaBonusesAttachment(map);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
            ifSuccess="0";
        }
        return ifSuccess;
    }
    /**
     * 获取奖励考核数据
     * @param almApplicationSCode
     * @param fromDate
     * @param toDate
     * @return
     */
    @Override
    public List<SlaBonusesDTO> getBonuseInfo(String almApplicationSCode, String fromDate, String toDate) {
        Map map=new HashMap();
        map.put("almApplicationId",almApplicationSCode);
        map.put("fromDate",fromDate);
        map.put("toDate",toDate);
        List<SlaBonusesDTO> list=slaBonusesDAO.getBonuseInfo(map);
        return list;
    }

    @Override
    public void updateSlaListId(Map map){
        SlaBonusesDTO slaBonusesDTO = new SlaBonusesDTO();
        slaBonusesDTO.setAlmShortName(map.get("projectName").toString());
        String startString = map.get("startDate").toString();
        String endString = map.get("endDate").toString();
        slaBonusesDTO.setQueryStartDate(Timestamp.valueOf(startString));
        slaBonusesDTO.setQueryEndDate(Timestamp.valueOf(endString));
        slaBonusesDTO.setTSlaListId(map.get("listId").toString());
        slaBonusesDAO.updateSlaListId(slaBonusesDTO);
    }

    @Override
    public String ifAddBonuse(Map map) {
        String ifSuccess="1";
        try{
            if(map.get("bonuseDate")!=null&&!"".equals(map.get("bonuseDate").toString())&&!"null".equals(map.get("bonuseDate"))){
                // 获取当月第一天和最后一天
                Map dayMap=getFirstdayAndLastday(map);
                String fromDate=dayMap.get("firstday")+"";
                String toDate=dayMap.get("lastday")+"";
                map.put("fromDate",fromDate);
                map.put("toDate",toDate);
                List<SlaBonusesDTO> slaBonusesList = slaBonusesDAO.getSlaBonusesList(map);
                if(slaBonusesList.size()>0){
                    //已经奖励过一条，不允许奖励了
                    ifSuccess="0";
                }else{
                    ifSuccess="1";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
            ifSuccess="0";
        }
        return ifSuccess;
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
                            if(!listName.contains(infolist.get(0).getAlmShortName())) {
                                listName.add(infolist.get(0).getAlmShortName());
                            }
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
    public static Map getFirstdayAndLastday(Map map) throws ParseException {
        Map dayMap=new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday, lastday;
        Calendar cale = null;
        // 获取前月的第一天
        cale = Calendar.getInstance();
        cale.setTime(format.parse(map.get("bonuseDate").toString()));
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        // 获取前月的最后一天
        cale = Calendar.getInstance();
        cale.setTime(format.parse(map.get("bonuseDate").toString()));
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime());
        System.out.println("本月第一天和最后一天分别是 ： " + firstday + " and " + lastday);
        dayMap.put("firstday",firstday);
        dayMap.put("lastday",lastday);
        return dayMap;
    }
}
