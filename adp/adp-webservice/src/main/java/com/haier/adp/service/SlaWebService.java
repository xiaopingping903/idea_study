package com.haier.adp.service;

import com.haier.adp.dto.AlmResultDTO;
import com.haier.adp.dto.ReturnValue;
import com.haier.adp.sla.dto.AlmReturnValue;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by Administrator on 2017/1/20.
 */
@WebService
public interface SlaWebService {
    @WebMethod
    AlmResultDTO getCheckData(String listData);

    @WebMethod
    ReturnValue getSlaData(String listData);

    @WebMethod
    ReturnValue insertResourcePoolDetailData(String listData);
}
