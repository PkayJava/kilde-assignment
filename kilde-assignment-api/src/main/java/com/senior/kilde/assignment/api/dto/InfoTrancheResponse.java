package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.senior.kilde.assignment.dao.enums.TrancheStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class InfoTrancheResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("annualInterest")
    private Float annualInterest;

    @JsonProperty("amountAvailableForInvestment")
    private BigDecimal amountAvailableForInvestment;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("minimumInvestmentAmount")
    private BigDecimal minimumInvestmentAmount;

    @JsonProperty("maximumInvestmentAmount")
    private BigDecimal maximumInvestmentAmount;

    @JsonProperty("maximumInvestmentAmountPerInvestor")
    private BigDecimal maximumInvestmentAmountPerInvestor;

    @JsonProperty("status")
    private TrancheStatus status;

}
