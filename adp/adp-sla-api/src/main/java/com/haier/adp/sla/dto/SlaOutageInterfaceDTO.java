package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
@Data
public class SlaOutageInterfaceDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private int totalTime;//宕机时间
    private String percent;//宕机稳定性
    private int totalNum;//宕机次数
    private double totalCutPayment;//总的扣款人天
    private List<SlaOutageDTO> list;//宕机详情
    private int annualDeductScore;//扣分
    private double annualCutPayment;//扣款
    private int sTime;//对应供应商的宕机时间
    private int sNum;//对应供应商的宕机次数
    private double stotalCutPayment;//对应供应商的扣款
    private String message;
}
