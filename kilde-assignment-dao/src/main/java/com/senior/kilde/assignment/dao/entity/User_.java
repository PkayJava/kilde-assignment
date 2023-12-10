package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(User.class)
public abstract class User_ {

    public static volatile SingularAttribute<User, String> id;
    public static volatile SingularAttribute<User, String> login;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, Boolean> enabled;
    public static volatile SingularAttribute<User, Boolean> accountNonExpired;
    public static volatile SingularAttribute<User, Boolean> accountNonLocked;
    public static volatile SingularAttribute<User, Boolean> credentialsNonExpired;
    public static volatile MapAttribute<User, String, Group> groups;
    public static volatile MapAttribute<User, String, Role> roles;
    public static volatile MapAttribute<User, String, Role> denyRoles;

}