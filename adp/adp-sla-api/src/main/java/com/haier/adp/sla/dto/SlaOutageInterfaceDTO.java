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
    private int totalCutPayment;//总的扣款人天
    private List<SlaOutageDTO> list;//宕机详情
    private int annualDeductScore;//扣分
    private int annualCutPayment;//扣款

}
