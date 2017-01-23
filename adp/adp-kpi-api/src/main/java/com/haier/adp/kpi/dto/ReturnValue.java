package com.haier.adp.kpi.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by 01435337 on 2016/11/18.
 */
@Data
public class ReturnValue implements Serializable {
    private static final long serialVersionUID = 1198725940096104982L;
    /**
     * 返回值成功与否标志位
     */
    private boolean success;

    /**
     * 返回值内容
     */
    private Object content;
}
