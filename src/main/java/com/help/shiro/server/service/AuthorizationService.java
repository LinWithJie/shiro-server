package com.help.shiro.server.service;

import com.help.shiro.core.remote.PermissionContext;
import com.help.shiro.server.dao.PermissionDao;
import com.help.shiro.server.dao.RoleDao;
import com.help.shiro.server.dao.UserDao;
import com.help.shiro.server.domain.Permission;
import com.help.shiro.server.domain.Role;
import com.help.shiro.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author created by BangZhuLi
 * @date 2018/2/7  16:10
 */
@Service
public class AuthorizationService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserDao userDao;

    /**
     * 根据用户Id查找其角色
     * @param userId
     * @return
     */
    public PermissionContext findPermissionContext(String userId) {
        User user = userDao.findOne(userId);
        if(user == null) {
            return null;
        }
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        for (Role role : user.getRoleList()) {
            roles.add(role.getRole());
            for (Permission permission : role.getPermissions()) {
                permissions.add(permission.getName());
            }
        }
        PermissionContext context = new PermissionContext();
        context.setPermissions(permissions);
        context.setRoles(roles);
        return context;
    }

}
