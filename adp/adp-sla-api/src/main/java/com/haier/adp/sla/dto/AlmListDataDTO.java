package com.haier.adp.sla.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rsdeep on 2016/12/19.
 */
@Data
public class AlmListDataDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    private String almApplicationSCodes;// Scode
    private String taskIds;//关联的taskID列表
    private String stageStartDate; //开始日
    private String stageEndDate; //结束日
    private String contractStageDesc; //付款阶段描述
    private String supplierId;
    private String supplier;
    private String creatorId;
    private String creatorName;
    private double totalCutPayment;
}
