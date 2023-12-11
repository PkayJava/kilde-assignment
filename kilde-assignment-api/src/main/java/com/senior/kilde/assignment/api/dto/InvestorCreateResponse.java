package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestorCreateResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("balance")
    private Double balance;

    @JsonProperty("version")
    private Long version;

}
