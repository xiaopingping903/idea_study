package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class SlaALMJIRARelationLogDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private String updateStatus;

    private Integer tSlaAlmJiraRelationHistoryId;

    private String ifDel;

}