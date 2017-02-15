package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.sf.json.JSONArray;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class SlaMonitorDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String almShortName;

    private String almApplicationId;

    private String projectId;

    private String projectName;

    private String appName;

    private int invokedTotal;

    private int invokedSuccessTotal;

    private int invokedFailTotal;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    private Date monitorDate;

    private String operator;

    private String operatorId;

    private Integer paasId;

    private String status;

    private String ifDel;

    private Long tSlaListId;

    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp operatorTime;

    private String remark1;

    private String remark2;

    private String assessStatus;//考核状态 0:不考核；1考核
    private String cancelAssessReason;//取消考核原因
    private JSONArray supplierList;
    private Date queryStartDate;
    private Date queryEndDate;

}