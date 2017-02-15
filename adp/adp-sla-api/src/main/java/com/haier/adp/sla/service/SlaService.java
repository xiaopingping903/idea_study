package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.AlmResult;
import com.haier.adp.sla.dto.AlmReturnValue;

/**
 * Created by rsdeep on 2017/1/13.
 */
public interface SlaService {
    AlmResult querySlaData(Object listData);

    AlmReturnValue insertResourcePoolListData(Object listData);

    AlmReturnValue insertResourcePoolDetailData(Object listData);
}
