package com.help.shiro.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author created by BangZhuLi
 * @date 2018/1/29  9:53
 * 权限表，权限控制
 */
@Data
@ToString(exclude = "roles")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"permission","name","url"}))
public class Permission implements Serializable {
    private static final long serialVersionUID = -3843846599026241981L;

    @Id
    @GeneratedValue
    private Integer id;//主键.

    private String name;//名称.

    @Column(columnDefinition="enum('menu','button')")
    private String resourceType;//资源类型，[menu|button]

    private String url;//资源路径.

    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view

    private Long parentId; //父编号

    private String parentIds; //父编号列表

    private Boolean available = Boolean.FALSE;

    @ManyToMany()
    @JoinTable(name="RolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    @JsonIgnore
    private List<Role> roles;
}
