package com.haier.adp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by 01435337 on 2016/11/4.
 */
@Data
public class ResponseResult implements Serializable {
    private static final long serialVersionUID = -3250379077810927776L;

    /**
     * 结果成功/失败Flag
     */
    private boolean success;

    /**
     * 结果内容
     */
    private Object content;
}
