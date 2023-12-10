package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "tbl_user_group")
@Getter
@Setter
public class UserGroup implements Serializable {

    @Id
    @Column(name = "user_group_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "user_id")
    private String userId;

}
