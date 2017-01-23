package com.haier.adp.kpi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * PPQA信息
 *
 * Created by 01435337 on 2016/11/16.
 */
@Data
public class PPQAInfo implements Serializable {
    private static final long serialVersionUID = 2855767534149508974L;
    /**
     * ID
     */
    private int id;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * PPQA得分
     */
    private float score;

    /**
     * 作者姓名
     */
    private String authorName;
}
