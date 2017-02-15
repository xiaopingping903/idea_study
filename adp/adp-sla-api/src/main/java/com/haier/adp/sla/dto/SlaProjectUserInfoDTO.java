package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by rsdeep on 2017/1/16.
 */
@Data
public class SlaProjectUserInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String userId;
    private String userName;
    private String email;
    private String role;
    private String account;
    private int tSlaProjectInfoId;
}
