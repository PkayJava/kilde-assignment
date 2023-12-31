package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvestorCreateRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("initialBalanceAmount")
    private BigDecimal initialBalanceAmount;

}
