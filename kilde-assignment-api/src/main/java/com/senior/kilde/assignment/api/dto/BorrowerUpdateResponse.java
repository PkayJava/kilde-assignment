package com.senior.kilde.assignment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowerUpdateResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("version")
    private Long version;

}
