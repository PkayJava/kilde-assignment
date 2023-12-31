package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class InvestmentInvestRequest {

    @JsonProperty("trancheName")
    private String trancheName;

    @JsonProperty("investorName")
    private String investorName;

    @JsonProperty("amount")
    private BigDecimal amount;

}
