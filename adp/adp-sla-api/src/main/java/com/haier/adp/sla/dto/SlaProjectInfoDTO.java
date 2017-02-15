package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SlaProjectInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String almApplicationId;

    private String almShortName;

    private String projectName;

    private String projectId;

    private List<SlaProjectInfoDTO> projectList;
}