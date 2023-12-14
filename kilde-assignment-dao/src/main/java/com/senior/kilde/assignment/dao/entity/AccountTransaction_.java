package com.senior.kilde.assignment.dao.entity;

import com.senior.kilde.assignment.dao.enums.AccountTransactionType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

import java.math.BigDecimal;
import java.util.Date;

@StaticMetamodel(AccountTransaction.class)
public abstract class AccountTransaction_ {

    public static volatile SingularAttribute<AccountTransaction, String> id;
    public static volatile SingularAttribute<AccountTransaction, Account> account;
    public static volatile SingularAttribute<AccountTransaction, AccountTransactionType> type;
    public static volatile SingularAttribute<AccountTransaction, Date> createdDate;
    public static volatile SingularAttribute<AccountTransaction, String> note;
    public static volatile SingularAttribute<AccountTransaction, BigDecimal> amount;
    public static volatile SingularAttribute<AccountTransaction, Long> version;

}