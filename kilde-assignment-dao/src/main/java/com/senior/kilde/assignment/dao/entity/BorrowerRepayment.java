package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "loan_amount")
    private BigDecimal loanAmount;

    @Column(name = "loan_duration")
    private Integer loanDuration;

    @Column(name = "month_payment_count")
    private Integer monthPaymentCount;

    @Column(name = "origin_payment_day")
    private Integer originPaymentDay;

    @Column(name = "borrower_fee_per_month")
    private BigDecimal borrowerFeePerMonth;

    @Column(name = "interest_per_month")
    private BigDecimal interestPerMonth;

    @Column(name = "total_outstanding_amount")
    private BigDecimal totalOutstandingAmount;

    @Column(name = "total_repayment_amount")
    private BigDecimal totalRepaymentAmount;

    @Column(name = "next_payment_date")
    private Date nextPaymentDate;

    @Column(name = "next_payment_back_date")
    private Date nextPaymentBackDate;

    @Column(name = "next_payment_amount")
    private BigDecimal nextPaymentAmount;

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
