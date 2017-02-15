package com.haier.adp.sla.controller;

import com.haier.adp.sla.dto.SlaOutageDTO;
import com.haier.adp.sla.dto.SlaProjectInfoDTO;
import com.haier.adp.sla.service.SlaOutageService;
import com.haier.adp.sla.service.SlaProjectInfoService;
import com.haier.adp.util.TestGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Controller
@RequestMapping("/adp/sla/outage")
public class SlaOutageController {
    @Autowired
    private SlaOutageService slaOutageService;
    @Autowired
    private SlaProjectInfoService slaProjectInfoService;
    @Value("${com.project.serverName}")
    private String serverName;
    /**
     * 宕机确认画面 查询
     * @param map 包含 projectName 应用简称名称,s_code S码 ,fromDate 宕机开始时间,toDate 宕机结束时间 pageNo 第几页,pageSize 每页多少条 tSlaListId tSlaListId主键
     * thirdUid portal账号
     * @return
     */
    @RequestMapping(value = "/getSlaOutageList", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object>  getSlaOutageList(@RequestBody Map map, HttpServletRequest request) {
        String fromServerName=request.getServerName();
        Map<String, Object> result = new HashMap<String, Object>();
        if(null!=map.get("fromDate")){
            map.put("fromDate",map.get("fromDate")+" 00:00:00");
        }
        if(null!=map.get("toDate")+" 23:59:59"){
            map.put("toDate",map.get("toDate")+" 23:59:59");
        }
        if(null!=map.get("thirdUid")&&fromServerName.equals(serverName)){
            Map mmm=getRole(map.get("thirdUid")+"");
            map.put("type",mmm.get("type"));
            map.put("listName",mmm.get("listName"));
            List<SlaOutageDTO> list=slaOutageService.getSlaOutageList(map);
            result.put("list",list);
            if(list.size()>0){
                map.put("pageNo",null);
                map.put("pageSize",null);
                result.put("total",slaOutageService.querySlaOutageListCount(map));
            }else{
                result.put("total",0);
            }
        }else{
            result.put("list",null);
            result.put("total",0);
        }
        return result;
    }

    /**
     * 根据用户账户获取角色
     * @param thirdUid
     * @return
     */
    public Map getRole(String thirdUid){
        return slaOutageService.getRole(thirdUid);
    }
    /**
     * 查询供应商信息
     * @return map projectName 应用简称
     */
   @RequestMapping(value = "/getSlaSupplierList",  method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> getSlaSupplierList(@RequestBody  Map map, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
       String fromServerName=request.getServerName();
       if(fromServerName.equals(serverName)){
           result.put("list",slaOutageService.getSlaSupplierList(map));
       }else{
           result.put("list",null);
       }
        return result;
    }

    /**
     * 更新宕机信息
     */
    @RequestMapping(value = "/updateSlaOutageData", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> updateSlaOutageData(@RequestBody Map map,HttpServletRequest request){
        String fromServerName=request.getServerName();
        Map<String, Object> result = new HashMap<String, Object>();
        String ifSuccess="1";
        try{
            if(fromServerName.equals(serverName)){
                slaOutageService.updateSlaOutageData(map);
            }else{
                ifSuccess="0";
            }
        }catch (Exception e){
            e.printStackTrace();
            ifSuccess="0";
        }
        result.put("ifSuccess",ifSuccess);
        return result;
    }
    /**
     * 根据id获取宕机信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/getSlaOutage", method = RequestMethod.GET)
    public @ResponseBody SlaOutageDTO getSlaOutage(@RequestParam int id) {
        return slaOutageService.getSlaOutage(id);
    }

    /**
     * 模拟插入
     */
    @RequestMapping(value = "/testInsertOutage", method = RequestMethod.GET)
    public @ResponseBody Map testInsertOutage() {
        Date date = new Date();
        for (int j = 0; j < 10; j++) {
            date = new Date(new Date().getTime() - j * 24 * 60 * 60 * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start_time = sdf.format(date) + " 00:00:00";
            String end_time = sdf.format(date) + " 23:59:59";
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < 100; i++) {
                SlaOutageDTO sladto = new SlaOutageDTO();
                //获得项目信息列表
                List<SlaProjectInfoDTO> slaProjectInfoList = slaProjectInfoService.getSlaProjectInfoList(new HashMap());
                Random r = new Random();
                SlaProjectInfoDTO slaProjectInfo = slaProjectInfoList.get(r.nextInt(slaProjectInfoList.size()));
                sladto.setAlmShortName(slaProjectInfo.getAlmShortName());
                sladto.setAlmApplicationId(slaProjectInfo.getAlmApplicationId());
                sladto.setProjectId(slaProjectInfo.getProjectId());
                sladto.setProjectName(slaProjectInfo.getProjectName());
                sladto.setCreateTime(new Timestamp(new Date().getTime()));
                long t1 = 0;
                long t2 = 0;
                Date randomDate1;
                Date randomDate2;
                String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
                while (true) {

                    randomDate1 = TestGenerator.randomDate(start_time, end_time);
                    randomDate2 = TestGenerator.randomDate(start_time, end_time);
                    t1 = randomDate1.getTime();
                    t2 = randomDate2.getTime();
                    if (t1 >= t2) {
                    } else {
                        break;
                    }
                }
                System.out.println(sdf.format(randomDate1));
                System.out.println(sdf.format(randomDate2));
                sladto.setOutageStartDate(new Timestamp(randomDate1.getTime()));
                sladto.setOutageEndDate(new Timestamp(randomDate2.getTime()));
                sladto.setServiceId(i + "");
                sladto.setServiceName(str[r.nextInt(str.length)]);
                int ti = (int) (t2 - t1) / 1000 / 60;
                sladto.setOutageTime(ti);
                sladto.setIfOvertime(ti > 30 ? "1" : "0");
                if (ti > 30) {
                    sladto.setDeductScore(10);
                    sladto.setCutPayment(2);
                }
                sladto.setIfNotRun("0");
                sladto.setStatus("0");
                sladto.setIfDel("0");
                sladto.setAssessStatus("1");
                sladto.setLostMoney(0);
                slaOutageService.insertSlaOutagelData(sladto);
            }
        }
        Map map=new HashMap();
        map.put("status","执行完成");
        return map;
    }
}
