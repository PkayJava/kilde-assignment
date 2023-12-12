package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class InvestmentBorrowRequest {

    @JsonProperty("trancheName")
    private String trancheName;

    @JsonProperty("borrowerName")
    private String borrowerName;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("duration")
    private Integer duration;

}
