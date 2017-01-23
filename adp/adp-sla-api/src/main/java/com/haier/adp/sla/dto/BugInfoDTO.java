package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by rsdeep on 2016/12/26.
 */
@Data
public class BugInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; //id
    private int listId;
    private String type;
    private int quantity;
    private String link;
    private int cutPoints;
    private double cutPayment;
    private String projectName;
}
