package com.haier.adp.sla.controller;

import com.haier.adp.sla.service.SlaALMJIRARelationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Controller
@RequestMapping("/adp/sla/relation")
public class SlaALMJIRARelationHistoryController {
    @Autowired
    private SlaALMJIRARelationHistoryService slaALMJIRARelationHistoryService;
    @Value("${com.project.serverName}")
    private String serverName;

    /**
     * 获得alm-jira关联关系
     * @param map almRequestId需求id almTaskId任务id projectName 应用简称 almAppScode s码
     * @return
     */
    @RequestMapping(value = "/getSlaALMJIRARelationList", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> getSlaALMJIRARelationList(@RequestBody Map map, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fromServerName=request.getServerName();
        //根据alm需求号任务号查所有的jira关联关系
        //查已经付款的jira关联关系
        /*List<SlaDetailDTO> slaDetailDTOList=slaALMJIRARelationHistoryService.getSlaALMJIRARelationList(map);
        result.put("list",slaDetailDTOList);*/
       /* if(slaDetailDTOList.size()>0){
            map.put("pageNo",null);
            map.put("pageSize",null);
            result.put("total",slaALMJIRARelationHistoryService.getSlaALMJIRARelationList(map).size());
        }else{
            result.put("total",0);
        }*/
        return result;
    }
    /**
     * 获得未关联的jira信息
     * @param map jiraStoryId任务id
     * @return
     */
    @RequestMapping(value = "/getJIRAList", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> getJIRAList(@RequestBody Map map,HttpServletRequest request) {
        String fromServerName=request.getServerName();
        Map<String, Object> result = new HashMap<String, Object>();
        //等待接口
        result.put("list",null);
        return result;
    }
    /**
     * 更新关联关系
     * @param map
     * @return
     */
    @RequestMapping(value = "/updateRelation", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> updateRelation(@RequestBody Map map,HttpServletRequest request) {
        String fromServerName=request.getServerName();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("ifSuccess",slaALMJIRARelationHistoryService.updateRelation(map));
        return result;
    }
}
