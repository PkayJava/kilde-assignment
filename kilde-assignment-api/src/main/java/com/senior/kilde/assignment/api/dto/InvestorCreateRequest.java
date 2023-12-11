package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestorCreateRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("initialBalanceAmount")
    private Double initialBalanceAmount;

}
