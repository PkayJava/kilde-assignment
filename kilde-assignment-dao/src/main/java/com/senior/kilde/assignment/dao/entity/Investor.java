package com.senior.kilde.assignment.dao.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "tbl_investor")
@Getter
@Setter
public class Investor implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "investor_id")
    @Setter(AccessLevel.NONE)
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
