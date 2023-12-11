package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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
    private Double amountAvailableForInvestment;

    @JsonProperty("minimumInvestmentAmount")
    private Double minimumInvestmentAmount;

    @JsonProperty("maximumInvestmentAmountPerInvestor")
    private Double maximumInvestmentAmountPerInvestor;

}
