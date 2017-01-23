package com.haier.adp.kpi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 01435337 on 2016/11/16.
 */
@Data
public class PPQASearchCondition implements Serializable {
    private static final long serialVersionUID = 349404535966031539L;
    /**
     * PPQA周期开始日
     */
    private Date startDate;

    /**
     * PPQA周期结束日
     */
    private Date endDate;
}
