package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class InfoBorrowerResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("accountNo")
    private String accountNo;

}
