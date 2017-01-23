package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by rsdeep on 2016/12/27.
 */
@Data
public class SlaProjectRelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    private int id;
    private int listId;
    private String projectName;
}
