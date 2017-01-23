package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Data
public class SlaOutageOperationDetailsDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;//自增主键
    private String operator;//操作人
    private String operatorId;//操作人id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;//创建时间
    private Integer tSlaOutageId;//t_sla_outage表id
    private Integer tSlaMonitorId;//t_sla_monitor表id
    private String content;//操作内容
    private String type;//1:宕机；2：dubbo异常
}
