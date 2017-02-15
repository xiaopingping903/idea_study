package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class
SlaBonusesDetailDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;

    private Integer tSlaBonusesTypeId;

    private String bonuseTitle;

    private Double bonuseMoney;

    private Integer bonuseScore;

    private String attachment;

    private String url;

    private Integer tSlaBonusesId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private String operator;

    private String operatorId;

    private String bonuseContent;

    private String selected;
    private String ifDel;
    private String uid;
    private List fileList;
    private String detailId;
}