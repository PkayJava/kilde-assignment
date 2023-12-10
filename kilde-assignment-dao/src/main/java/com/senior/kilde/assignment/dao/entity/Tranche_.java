package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Tranche.class)
public abstract class Tranche_ {

    public static volatile SingularAttribute<Tranche, String> id;
    public static volatile SingularAttribute<Tranche, String> name;
    public static volatile SingularAttribute<Tranche, Float> annualInterest;
    public static volatile SingularAttribute<Tranche, Double> amountAvailableForInvestment;
    public static volatile SingularAttribute<Tranche, Integer> duration;
    public static volatile SingularAttribute<Tranche, Double> minimumInvestmentAmount;
    public static volatile SingularAttribute<Tranche, Double> maximumInvestmentAmount;
    public static volatile SingularAttribute<Tranche, Double> maximumInvestmentAmountPerInvestor;

}