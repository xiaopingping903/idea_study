package com.haier.adp.sla.dto;

import lombok.Data;

/**
 * Created by rsdeep on 2017/1/18.
 * 接收从PAAS传递来的角色信息，只需要角色名字，ID，权限。
 * 名字和ID在单独的字符串中，此DTO只保存权限信息。
 */
@Data
public class RoleInfoDTO {
    private String roleKey;
    private int targetId;
}
