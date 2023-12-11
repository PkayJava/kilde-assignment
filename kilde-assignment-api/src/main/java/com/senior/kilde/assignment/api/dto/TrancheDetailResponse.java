package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TrancheDetailResponse {

    @JsonProperty("id")
    private String id;

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

    @JsonProperty("version")
    private Long version;

}
