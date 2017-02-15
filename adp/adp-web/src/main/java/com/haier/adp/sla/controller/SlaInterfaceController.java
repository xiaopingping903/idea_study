package com.haier.adp.sla.controller;

import com.haier.adp.sla.dto.SlaMonitorInterfaceDTO;
import com.haier.adp.sla.dto.SlaOutageInterfaceDTO;
import com.haier.adp.sla.service.SlaInterfaceService;
import com.haier.adp.sla.service.SlaTOPaasInterfaceService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/20.
 */
@Controller
@RequestMapping("/adp/sla/interface")
public class SlaInterfaceController {
    @Autowired
    private SlaInterfaceService slaInterfaceService;
    @Autowired
    private SlaTOPaasInterfaceService slaTOPaasInterfaceService;

    @RequestMapping(value = "/getStoryList", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> getClosedStoryList(@RequestBody Map map) {
        Map<String, Object> result = new HashMap<String, Object>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String almShortName=map.get("almShortName")+"";
        try {
            Date  fromDate=sdf.parse(map.get("fromDate")+"");
            Date toDate=sdf.parse(map.get("toDate")+"");
            int pageNo=Integer.parseInt(map.get("pageNo")+"");
            int pageSize=Integer.parseInt(map.get("pageSize")+"");
            result.put("list",slaInterfaceService.getClosedStoryList(almShortName,fromDate,toDate,pageNo,pageSize));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/createJenkinsJob", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> createJenkinsJob(@RequestBody Map map) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("ifSuccess",slaTOPaasInterfaceService.createJenkinsJob("efererv"));
        return result;
    }
    @RequestMapping(value = "/executeJenkinsJob", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> executeJenkinsJob(@RequestBody Map map) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONArray array=JSONArray.fromObject(map.get("projectList"));
        result.put("ifSuccess","1");
        if(array.size()>0){
            for (int i = 0; i <array.size() ; i++) {
                JSONObject obj = (JSONObject) array.get(i);
                String projectName = obj.get("projectName") + "";
               slaTOPaasInterfaceService.executeJenkinsJob(projectName);
            }
            result.put("ifSuccess","1");
        }else {
            result.put("ifSuccess","0");
        }
        return result;
    }
    @RequestMapping(value = "/updateStoryReleaseTime", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> updateStoryReleaseTime(@RequestBody Map map) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map pp=new HashMap();
        pp.put("DEV-186","2012-12-12 12:12:12");
        result.put("ifSuccess",slaTOPaasInterfaceService.updateStoryReleaseTime(pp));
        return result;
    }

    @RequestMapping(value = "/getOutageInfo", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> getOutageInfo(@RequestBody Map map) {
        Map<String, Object> result = new HashMap<String, Object>();
        SlaOutageInterfaceDTO dto=slaInterfaceService.getOutageInfo("",map.get("fromDate")+"",map.get("toDate")+"","");
        return result;
    }
    @RequestMapping(value = "/getMointorInfo", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> getMointorInfo(@RequestBody Map map) {
        Map<String, Object> result = new HashMap<String, Object>();
        SlaMonitorInterfaceDTO dto=slaInterfaceService.getMointorInfo("","","","");
        return result;
    }


}
