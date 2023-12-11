package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_borrower_repayment")
@Getter
@Setter
public class BorrowerRepayment implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "borrower_repayment_id")
    @Setter(AccessLevel.NONE)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tranche_id", referencedColumnName = "tranche_id")
    private Tranche tranche;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", referencedColumnName = "borrower_id")
    private Borrower borrower;

    @Column(name = "loan_amount")
    private Double loanAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "month_payment_count")
    private Integer monthPaymentCount;

    @Column(name = "next_payment_date")
    private Date nextPaymentDate;

    @Column(name = "next_payment_back_date")
    private Date nextPaymentBackDate;

    @Column(name = "next_payment_amount")
    private Double nextPaymentAmount;

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
