package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by rsdeep on 2017/2/10.
 */
@Data
public class AlmReturnValue implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 返回值成功与否标志位
     */
    private String flag;

    /**
     * 返回值内容
     */
    private String content;
}
