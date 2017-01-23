package com.haier.adp.sla.controller;

import com.haier.adp.sla.dto.SlaMonitorDTO;
import com.haier.adp.sla.dto.SlaProjectInfoDTO;
import com.haier.adp.sla.service.SlaMonitorService;
import com.haier.adp.sla.service.SlaProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Controller
@RequestMapping("/adp/sla/monitor")
public class SlaMonitorController{
    @Autowired
    private SlaMonitorService slaMonitorService;
    @Autowired
    private SlaProjectInfoService slaProjectInfoService;
    /**
     * 获取dubbo异常信息列表
     * @param map
     * @return
     */
    @RequestMapping(value="/getSlaMonitorList",method =RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String ,Object> getSlaMonitorList(@RequestBody  Map map){
        Map<String ,Object> result=new HashMap<String,Object>();
        result.put("list",slaMonitorService.getSlaMonitorList(map));
        map.put("pageNo",null);
        map.put("pageSize",null);
        result.put("total",slaMonitorService.querySlaMonitorListCount(map));
        return result;
    }
    /**
     * 更新dubbo异常信息
     */
    @RequestMapping(value = "/updateSlaMonitorData", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody void updateSlaMonitorData(@RequestBody Map map){
        slaMonitorService.updateSlaMonitorData(map);
    }

    /**
     * 模拟插入
     */
    @RequestMapping(value = "/testInsertMonitor", method = RequestMethod.GET)
    public @ResponseBody Map testInsertMonitor() {
        Date date=new Date();
        for (int j = 0; j <10 ; j++) {
            date = new Date(new Date().getTime()-j*24*60*60*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start_time = sdf.format(date) + " 00:00:00";
            String end_time = sdf.format(date) + " 23:59:59";
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
            for (int i = 0; i < 100; i++) {
                SlaMonitorDTO sladto = new SlaMonitorDTO();
                //获得项目信息列表
                List<SlaProjectInfoDTO> slaProjectInfoList = slaProjectInfoService.getSlaProjectInfoList(new HashMap());
                Random r = new Random();
                SlaProjectInfoDTO slaProjectInfo = slaProjectInfoList.get(r.nextInt(slaProjectInfoList.size()));
                sladto.setAlmShortName(slaProjectInfo.getAlmShortName());
                sladto.setAlmApplicationId(slaProjectInfo.getAlmApplicationId());
                sladto.setProjectId(slaProjectInfo.getProjectId());
                sladto.setProjectName(slaProjectInfo.getProjectName());
                sladto.setCreateTime(new Timestamp(new Date().getTime()));
                int failNum=r.nextInt(10);
                int successNum=r.nextInt(10);
                int total=failNum+successNum;
                sladto.setInvokedFailTotal(failNum);
                sladto.setInvokedSuccessTotal(successNum);
                sladto.setInvokedTotal(total);
                sladto.setMonitorDate(new Date());
                sladto.setAppName(str[r.nextInt(str.length)]);
                sladto.setStatus("0");
                sladto.setIfDel("0");
                sladto.setAssessStatus("1");
                slaMonitorService.insertSlaMonitor(sladto);
            }
        }
        Map map=new HashMap();
        map.put("status","执行完成");
        return map;
    }
}
