package com.help.shiro.server.dao;

import com.help.shiro.server.domain.Permission;
import com.help.shiro.server.domain.Role;
import com.help.shiro.server.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author created by BangZhuLi
 * @date 2018/2/7  16:11
 */
@Repository
public interface PermissionDao extends JpaRepository<Permission,Integer> {

    Page<Permission> findAll(Specification specification, Pageable pageable);

    List<Permission> findByRoles(List<Role> roles);

}
