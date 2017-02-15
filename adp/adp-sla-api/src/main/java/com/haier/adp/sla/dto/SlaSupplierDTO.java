package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SlaSupplierDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String supplierId;

    private String supplier;

    private String projectName;

    private String projectId;

    private String selected;

    private int percent;
}