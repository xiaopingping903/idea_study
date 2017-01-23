package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rsdeep on 2016/12/16.
 */
@Data
public class AlmDetailDataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String almRequestId; //alm需求号
    private String almTaskId; //alk任务号，此处为列表
    private String projectName;//ALM应用简称
    private String projectSCode;//ALM应用系统S码
    private boolean ifSatisfied;//是否满意
}
