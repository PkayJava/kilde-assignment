package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

import java.math.BigDecimal;
import java.util.Date;

@StaticMetamodel(BorrowerRepayment.class)
public abstract class BorrowerRepayment_ {

    public static volatile SingularAttribute<BorrowerRepayment, String> id;
    public static volatile SingularAttribute<BorrowerRepayment, BigDecimal> loanAmount;
    public static volatile SingularAttribute<BorrowerRepayment, Integer> duration;
    public static volatile SingularAttribute<BorrowerRepayment, Borrower> borrower;
    public static volatile SingularAttribute<BorrowerRepayment, Tranche> tranche;
    public static volatile SingularAttribute<BorrowerRepayment, Account> account;
    public static volatile SingularAttribute<BorrowerRepayment, Integer> monthPaymentCount;
    public static volatile SingularAttribute<BorrowerRepayment, Date> nextPaymentDate;
    public static volatile SingularAttribute<BorrowerRepayment, Date> nextPaymentBackDate;
    public static volatile SingularAttribute<BorrowerRepayment, BigDecimal> nextPaymentAmount;
    public static volatile SingularAttribute<BorrowerRepayment, Long> version;

}