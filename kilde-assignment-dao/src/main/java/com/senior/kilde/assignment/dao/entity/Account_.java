package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Account.class)
public abstract class Account_ {

    public static volatile SingularAttribute<Account, String> id;
    public static volatile SingularAttribute<Account, String> accountNo;
    public static volatile SingularAttribute<Account, Double> balance;
    public static volatile SingularAttribute<Account, Long> version;

}