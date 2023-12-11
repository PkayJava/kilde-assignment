package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvestmentInvestResponse {

    @JsonProperty("message")
    private String message;
    
}
