package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvestmentInvestRequest {

    @JsonProperty("trancheName")
    private String trancheName;

    @JsonProperty("investorName")
    private String investorName;

    @JsonProperty("amount")
    private Double amount;

}
