package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/4.
 */
@Data
public class SlaMonitorInterfaceDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private int invokedTotal;//总次数
    private String percent;//稳定性
    private int invokedSuccessTotal;//成功次数
    private int invokedFailTotal;//失败次数
    private double monitorCutPayment;//扣款人天
    private int monitorDeductScore;//扣分
    private double smonitorCutPayment;//对应供应商的扣款
}
