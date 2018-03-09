package com.help.shiro.server.vo;

import com.help.shiro.server.domain.Permission;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by BangZhuLi on 2018/3/9
 */
@Data
public class RolePermissionAllocation implements Serializable {
    private static final long serialVersionUID = -8953879168957885711L;

    //角色ID
    private Integer id;
    //角色type - role
    private String type;
    //角色Name - decctiption
    private String name;
    //权限Name列转行，以,分割
    private String permissionNames;
    //权限Id列转行，以‘,’分割
    private String permissionIds;
}
