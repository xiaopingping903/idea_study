package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
@Data
public class SlaALMJIRARelationHistoryDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String almRequestId;

    private String almTaskId;

    private String jiraEpicId;

    private String jiraStoryId;

    private String taskDesc;

    private String storyDesc;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createTime;

    private String operator;

    private String operatorId;

    private String slaUpdateStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp slaUpdateTime;

    private String almUpdateStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp almUpdateTime;

    private String jiraUpdateStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp jiraUpdateTime;

    private String ifDel;
    private String taskTitle;


}