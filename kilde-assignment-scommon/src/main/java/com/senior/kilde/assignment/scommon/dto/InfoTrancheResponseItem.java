package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InfoTrancheResponseItem {

    @JsonProperty("investorName")
    private String investorName;

    @JsonProperty("investedAmount")
    private BigDecimal investedAmount;

}
