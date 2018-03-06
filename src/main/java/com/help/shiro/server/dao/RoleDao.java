package com.help.shiro.server.dao;

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
public interface RoleDao extends JpaRepository<Role, Integer> {

    List<Role> findByUserInfos(List<User> users);

    Page<Role> findAll(Specification specification, Pageable pageable);
}
