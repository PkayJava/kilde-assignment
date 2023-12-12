package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ErrorResponse implements Serializable {

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
    private Date timestamp;

    @JsonProperty("path")
    private String path;

    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    private int status;

    @JsonProperty("error")
    private String error;

    @JsonProperty("exception")
    private String exception;

    @JsonProperty("traces")
    private List<String> traces;

    @JsonProperty("trace")
    private String trace;

}
