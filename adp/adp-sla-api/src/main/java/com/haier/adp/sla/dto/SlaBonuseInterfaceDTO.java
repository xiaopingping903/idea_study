package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
@Data
public class SlaBonuseInterfaceDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private double bonusesTotalMoney;//奖励人天
    private int bonusesTotalScore;//奖励分数
    private ArrayList<SlaBonusesDTO> list;
    private int bonusesTotalNum;//奖励次数
    private int perbonusesTotalScore;//供应商奖励分数
    private double perbonusesTotalMoney;//供应商奖励人天
    private int perbonusesTotalNum;//供应商奖励次数
    private String almApplicationSCode;//s码
}
