package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Borrower.class)
public abstract class Borrower_ {

    public static volatile SingularAttribute<Borrower, String> id;
    public static volatile SingularAttribute<Borrower, String> name;

}