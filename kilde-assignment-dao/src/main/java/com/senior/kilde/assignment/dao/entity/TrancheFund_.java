package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TrancheFund.class)
public abstract class TrancheFund_ {

    public static volatile SingularAttribute<TrancheFund, String> id;
    public static volatile SingularAttribute<TrancheFund, Tranche> tranche;
    public static volatile SingularAttribute<TrancheFund, Double> fundAmount;
    public static volatile SingularAttribute<TrancheFund, Investor> investor;
    public static volatile SingularAttribute<TrancheFund, Long> version;

}