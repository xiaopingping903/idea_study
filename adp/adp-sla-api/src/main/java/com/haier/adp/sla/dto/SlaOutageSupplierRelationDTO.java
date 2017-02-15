package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class SlaOutageSupplierRelationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String supplierId;

    private String supplier;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createTime;

    private int tSlaOutageId;
    private Integer tSlaMonitorId;//t_sla_monitor表id
    private String type;//1:宕机；2：dubbo异常
}