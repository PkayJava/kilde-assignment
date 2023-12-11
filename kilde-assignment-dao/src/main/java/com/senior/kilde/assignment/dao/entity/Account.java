package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "tbl_account")
@Getter
@Setter
public class Account implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    @Setter(AccessLevel.NONE)
    private String id;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "balance")
    private Double balance;

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
