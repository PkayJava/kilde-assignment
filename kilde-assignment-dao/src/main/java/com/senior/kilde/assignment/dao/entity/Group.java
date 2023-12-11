package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "tbl_group")
@Getter
@Setter
public class Group implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id")
    @Setter(AccessLevel.NONE)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_group_role",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    )
    @MapKeyColumn(name = "group_role_id")
    private Map<String, Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_user_group",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    )
    @MapKeyColumn(name = "user_group_id")
    private Map<String, User> users;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
