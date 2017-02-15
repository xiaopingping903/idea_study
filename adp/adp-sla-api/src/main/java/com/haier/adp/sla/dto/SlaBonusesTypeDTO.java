package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class SlaBonusesTypeDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String bonuseTitle;

    private Double bonuseMoney;

    private Integer bonuseScore;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private String bonuseContent;
}