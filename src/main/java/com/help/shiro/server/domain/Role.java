package com.help.shiro.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author created by BangZhuLi
 * @date 2018/1/29  9:51
 * 角色表，权限控制
 */
@Data
@ToString(exclude = "userInfos,permissions")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "role"))
public class Role implements Serializable {
    private static final long serialVersionUID = -6763607950688569217L;

    @Id
    @GeneratedValue
    private Integer id; // 编号


    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:

    private String description; // 角色描述,UI界面显示使用

    private Boolean available = Boolean.FALSE; // 是否可用,如果不可用将不会添加给用户

    //角色 -- 权限关系：多对多关系;
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="RolePermission",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="permissionId")})
    private List<Permission> permissions;

    // 用户 - 角色关系定义;
    @ManyToMany
    @JoinTable(name="UserRole",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="uid")})
    @JsonIgnore
    private List<User> userInfos;// 一个角色对应多个用户


}
