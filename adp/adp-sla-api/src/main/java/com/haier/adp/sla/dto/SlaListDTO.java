package com.haier.adp.sla.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Data
public class SlaListDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    private int id; //id
    private String contractStageDesc; //付款阶段描述 from ALM
    private String stageStartDate; //开始日 from ALM
    private String stageEndDate; //结束日 from ALM
    private String reportCreateDate; //报告生成日
    private int score; //得分 from ALM???可能根据下属taskId合计
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date queryStartDate; //查询开始日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date queryEndDate; //查询结束日期
    private String projectName; //项目名称 from ALM
    private String type; //SLA类型，分为资源池与运维类等
    // 如果类型为运维类，是否已付款
    private boolean ifPaid;
    // 如果在页面上已经删除，则IfShown为flase
    private boolean ifShown;
    private String taskIds;// from ALM
    private int startNo;
    private int pageSize;
    private String supplierId;
    private String supplier;
    private String creatorId;
    private String creatorName;
    private double totalCutPayment;
    private String almApplicationSCodes;
}
