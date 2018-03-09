package com.help.shiro.server.vo;

import com.help.shiro.server.domain.Role;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by BangZhuLi on 2018/3/9
 * 用于展示该用户在角色分配的选择角色时是否已经拥有该角色
 */
@Data
public class UserRoleMark extends Role implements Serializable {

    private static final long serialVersionUID = 4211341852126808744L;

    /**
     * 用户ID (用String， 考虑多个ID，现在只有一个ID)
     */
    private String userId;
    /**
     * 是否勾选
     */
    private String marker;

    public boolean isCheck(){
        return StringUtils.equals(userId,marker);
    }
}
