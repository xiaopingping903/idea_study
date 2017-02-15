package com.haier.adp.sla.controller;

import com.haier.adp.sla.dto.SlaBonusesDTO;
import com.haier.adp.sla.dto.SlaBonusesTypeDTO;
import com.haier.adp.sla.service.SlaBonuseService;
import com.haier.adp.util.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Controller
@RequestMapping("/adp/sla/bonuse")
public class SlaBonusesController {
    @Autowired
    private SlaBonuseService slaBonuseService;
    @Value("${com.project.serverName}")
    private String serverName;
    @Value("${com.project.serverPort}")
    private String serverPort;
    /**
     * sla奖励一览 查询
     * @param map 包含s_code s码， projectName 应用简称,fromDate 奖励开始时间,toDate 奖励结束时间 pageNo 第几页,pageSize 每页多少条 tSlaListId tSlaListId主键
     * thirdUid portal账号
     *@return
     */
    @RequestMapping(value = "/getSlaBonuseList", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> getSlaBonuseList(@RequestBody Map map,HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fromServerName=request.getServerName();
        if(null!=map.get("thirdUid")&&fromServerName.equals(serverName)) {
            Map mmm = getRole(map.get("thirdUid") + "");
            map.put("type", mmm.get("type"));
            map.put("listName", mmm.get("listName"));
            List<SlaBonusesDTO> slaBonusesDTOsList = slaBonuseService.getSlaBonusesList(map);
            result.put("list", slaBonusesDTOsList);
            if (slaBonusesDTOsList.size() > 0) {
                map.put("pageNo", null);
                map.put("pageSize", null);
                result.put("total", slaBonuseService.getSlaBonusesList(map).size());
            } else {
                result.put("total", 0);
            }
        }else{
            result.put("list",null);
            result.put("total",0);
        }
        return result;
    }

    private Map getRole(String thirdUid) {
        return slaBonuseService.getRole(thirdUid);
    }

    /**
     * 追加奖励
     * @param
     * @return
     */
    @RequestMapping(value = "/getBonusesTypeList", method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> getBonusesTypeList(HttpServletRequest request) {
        String fromServerName=request.getServerName();
        Map<String, Object> result = new HashMap<String, Object>();
        if(fromServerName.equals(serverName)){
            List<SlaBonusesTypeDTO> list=slaBonuseService.getBonusesTypeList();
            result.put("list",list);
        }else{
            result.put("list",null);
        }
        return result;
    }

    /**
     *保存追加
     * @param map
     * @return
     */
    @RequestMapping(value = "/saveAddSlaBonuse", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> saveAddSlaBonuse(@RequestBody Map map,HttpServletRequest request) {
        String fromServerName=request.getServerName();
        Map<String, Object> result = new HashMap<String, Object>();
        String ifSuccess="1";
        map.put("type","0");//添加
        if(fromServerName.equals(serverName)){
            ifSuccess = slaBonuseService.saveAddSlaBonuse(map);
        }else{
            ifSuccess="0";
        }
        result.put("ifSuccess",ifSuccess);
        return result;
    }
    /**
     *保存编辑
     * @param map
     * @return
     */
    @RequestMapping(value = "/saveUpdateSlaBonuse", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> saveUpdateSlaBonuse(@RequestBody Map map,HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fromServerName=request.getServerName();
        map.put("type","1");//更新
        String ifSuccess="1";
        if(fromServerName.equals(serverName)) {
            ifSuccess = slaBonuseService.saveAddSlaBonuse(map);
        }else{
            ifSuccess="0";
        }
        result.put("ifSuccess",ifSuccess);
        return result;
    }
    /**
     *删除奖励
     * @param
     * @return
     */
    @RequestMapping(value = "/delSlaBonuse",method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody Map<String,Object> delSlaBonuse(@RequestBody Map map,HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fromServerName=request.getServerName();
        String ifSuccess="1";
        if(fromServerName.equals(serverName)){
            ifSuccess = slaBonuseService.delSlaBonuse(map);
        }else{
            ifSuccess="0";
        }
        result.put("ifSuccess",ifSuccess);
        return result;
    }
    /**
     * 上传附件
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody Map<String,Object> handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        String fromServerName=request.getServerName();
        Map map=new HashMap();
        if(fromServerName.equals(serverName)){
            FileUpload fileUpload=new FileUpload();
            map = fileUpload.upload(file,request,serverName,serverPort);
        }else{
            map.put("flag","不允许来自"+fromServerName+"的访问");
        }
        return map;
    }

    /**
     * 每个月对应一个应用一个供应商仅能有一条奖励记录。
     * @return
     */
    @RequestMapping(value="/ifAddBonuse", method=RequestMethod.POST)
    public @ResponseBody Map<String,Object> ifAddBonuse(@RequestBody Map map,HttpServletRequest request){
        String fromServerName=request.getServerName();
        Map<String, Object> result = new HashMap<String, Object>();
        String ifSuccess="1";
        if(fromServerName.equals(serverName)){
            ifSuccess = slaBonuseService.ifAddBonuse(map);
        }else{
            ifSuccess="0";
        }
        result.put("ifSuccess",ifSuccess);
        return result;
    }
}
