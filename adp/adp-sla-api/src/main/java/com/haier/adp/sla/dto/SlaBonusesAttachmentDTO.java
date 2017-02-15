package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class SlaBonusesAttachmentDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String attachment;

    private String url;

    private String uid;

    private Integer tSlaBonusesDetailId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createTime;

}