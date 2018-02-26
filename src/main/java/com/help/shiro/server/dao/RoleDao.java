package com.help.shiro.server.dao;

import com.help.shiro.server.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author created by BangZhuLi
 * @date 2018/2/7  16:11
 */
@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {
}
