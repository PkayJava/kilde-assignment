package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestorDepositRequest {

    @JsonProperty("investorName")
    private String investorName;

    @JsonProperty("amount")
    private Double amount;

}
