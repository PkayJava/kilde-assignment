package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TrancheDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("annualInterest")
    private Float annualInterest;

    @JsonProperty("amountAvailableForInvestment")
    private Double amountAvailableForInvestment;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("minimumInvestmentAmount")
    private Double minimumInvestmentAmount;

    @JsonProperty("maximumInvestmentAmount")
    private Double maximumInvestmentAmount;

    @JsonProperty("maximumInvestmentAmountPerInvestor")
    private Double maximumInvestmentAmountPerInvestor;

}
