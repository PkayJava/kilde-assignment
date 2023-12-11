package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_tranche")
@Getter
@Setter
public class Tranche implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tranche_id")
    @Setter(AccessLevel.NONE)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "annual_interest")
    private Float annualInterest;

    @Column(name = "amount_available_for_investment")
    private BigDecimal amountAvailableForInvestment;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "minimum_investment_amount")
    private BigDecimal minimumInvestmentAmount;

    @Column(name = "maximum_investment_amount")
    private BigDecimal maximumInvestmentAmount;

    @Column(name = "maximum_investment_amount_per_investor")
    private BigDecimal maximumInvestmentAmountPerInvestor;

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
