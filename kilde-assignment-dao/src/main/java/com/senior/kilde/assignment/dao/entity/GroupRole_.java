package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(GroupRole.class)
public abstract class GroupRole_ {

    public static volatile SingularAttribute<GroupRole, String> id;
    public static volatile SingularAttribute<GroupRole, String> groupId;
    public static volatile SingularAttribute<GroupRole, String> roleId;

}