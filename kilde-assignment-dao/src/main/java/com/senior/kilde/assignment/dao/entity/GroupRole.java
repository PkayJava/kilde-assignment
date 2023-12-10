package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "tbl_group_role")
@Getter
@Setter
public class GroupRole implements Serializable {

    @Id
    @Column(name = "group_role_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private String id;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "role_id")
    private String roleId;

}