package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class InfoRepaymentResponseItem {

    @JsonProperty("month")
    private Integer month;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("paymentDate")
    private Date paymentDate;

    @JsonProperty("paymentAmount")
    private BigDecimal paymentAmount;

}
