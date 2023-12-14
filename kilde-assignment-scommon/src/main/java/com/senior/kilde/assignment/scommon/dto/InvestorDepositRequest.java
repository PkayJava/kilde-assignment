package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvestorDepositRequest {

    @JsonProperty("investorName")
    private String investorName;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("note")
    private String note;

}
