package com.help.shiro.server.vo;

import com.help.shiro.server.domain.Permission;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by BangZhuLi on 2018/3/9
 * 用于展示该在权限分配的选择权限时是否已经拥有该权限
 *
 */
@Data
public class RolePermissionMark extends Permission implements Serializable {
    private static final long serialVersionUID = 6781756013179721804L;

    /**
     * 是否勾选
     */
    private String marker;
    /**
     * role Id
     */
    private String roleId;

    public boolean isCheck(){
        return StringUtils.equals(roleId,marker);
    }
}
