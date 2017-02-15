package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SlaBonusesDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String almShortName;

    private String almApplicationId;

    private String bonuseType;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date bonuseDate;

    private Double bonuseMoney;

    private Integer bonuseScore;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private String operator;

    private String operatorId;

    private String tSlaListId;

    private String supplier;
    private String supplierId;
    private List<SlaBonusesDetailDTO> bonuseItemList;
    private String ifDel;

    private Date queryStartDate;
    private Date queryEndDate;

}