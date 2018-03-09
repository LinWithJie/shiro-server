package com.help.shiro.server.vo;

import com.help.shiro.server.domain.User;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by BangZhuLi on 2018/3/9
 * 用于展现用户角色分配页面的数据
 */
@Data
public class UserRoleAllocation extends User implements Serializable {
    private static final long serialVersionUID = 3261363942306024019L;

    //Role Name列转行，以,分割
    private String roleNames;

    //Role Id列转行，以‘,’分割
    private String roleIds;
}