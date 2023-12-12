package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvestmentBorrowResponse {

    @JsonProperty("trancheName")
    private String trancheName;

    @JsonProperty("borrowerName")
    private String borrowerName;

    @JsonProperty("amount")
    private Double amount;

}
