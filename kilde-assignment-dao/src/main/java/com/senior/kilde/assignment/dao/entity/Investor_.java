package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Investor.class)
public abstract class Investor_ {

    public static volatile SingularAttribute<Investor, String> id;
    public static volatile SingularAttribute<Investor, String> name;

}