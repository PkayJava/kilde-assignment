package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TrancheCreateRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("annualInterestRate")
    private Float annualInterestRate;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("amountAvailableForInvestment")
    private BigDecimal amountAvailableForInvestment;

    @JsonProperty("minimumInvestmentAmount")
    private BigDecimal minimumInvestmentAmount;

    @JsonProperty("maximumInvestmentAmountPerInvestor")
    private BigDecimal maximumInvestmentAmountPerInvestor;

}
