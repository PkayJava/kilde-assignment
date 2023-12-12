package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestorUpdateResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("version")
    private Long version;

}
