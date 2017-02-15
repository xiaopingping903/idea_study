package com.haier.adp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.haier.adp.dto.AlmResultDTO;
import com.haier.adp.dto.ReturnValue;
import com.haier.adp.service.SlaWebService;
import com.haier.adp.sla.dto.AlmResult;
import com.haier.adp.sla.dto.AlmReturnValue;
import com.haier.adp.sla.service.SlaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;

/**
 * Created by Administrator on 2017/2/8.
 */
@WebService(targetNamespace = "http://service.adp.haier.com/", endpointInterface = "com.haier.adp.service.SlaWebService")
public class SlaWebServiceImpl implements SlaWebService {
    @Autowired
    SlaService slaService;

    @Override
    public AlmResultDTO getCheckData(String listData) {
        JSONObject jsStr = JSONObject.parseObject(listData);
        AlmResultDTO result = new AlmResultDTO();
        AlmResult almResult = slaService.querySlaData(jsStr);
        BeanUtils.copyProperties(almResult, result);
        return result;
    }

    @Override
    public ReturnValue getSlaData(String listData) {
        ReturnValue returnValue = new ReturnValue();
        JSONObject jsStr = JSONObject.parseObject(listData);
        AlmReturnValue result = slaService.insertResourcePoolListData(jsStr);
        BeanUtils.copyProperties(result, returnValue);
        return returnValue;
    }

    @Override
    public ReturnValue insertResourcePoolDetailData(String listData) {
        ReturnValue returnValue = new ReturnValue();
        JSONObject jsStr = JSONObject.parseObject(listData);
        AlmReturnValue result = slaService.insertResourcePoolDetailData(jsStr);
        BeanUtils.copyProperties(result, returnValue);
        return returnValue;
    }
}
