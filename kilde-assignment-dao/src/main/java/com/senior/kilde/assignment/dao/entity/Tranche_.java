package com.senior.kilde.assignment.dao.entity;

import com.senior.kilde.assignment.dao.enums.TrancheStatus;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

import java.math.BigDecimal;

@StaticMetamodel(Tranche.class)
public abstract class Tranche_ {

    public static volatile SingularAttribute<Tranche, String> id;
    public static volatile SingularAttribute<Tranche, String> name;
    public static volatile SingularAttribute<Tranche, Float> annualInterest;
    public static volatile SingularAttribute<Tranche, BigDecimal> amountAvailableForInvestment;
    public static volatile SingularAttribute<Tranche, Integer> duration;
    public static volatile SingularAttribute<Tranche, BigDecimal> minimumInvestmentAmount;
    public static volatile SingularAttribute<Tranche, BigDecimal> maximumInvestmentAmount;
    public static volatile SingularAttribute<Tranche, BigDecimal> maximumInvestmentAmountPerInvestor;
    public static volatile SingularAttribute<Tranche, TrancheStatus> status;
    public static volatile SingularAttribute<Tranche, Long> version;

}