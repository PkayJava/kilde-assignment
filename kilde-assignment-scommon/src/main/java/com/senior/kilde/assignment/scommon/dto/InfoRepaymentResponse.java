package com.senior.kilde.assignment.scommon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class InfoRepaymentResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("loanAmount")
    private BigDecimal loanAmount;

    @JsonProperty("loanDuration")
    private Integer loanDuration;

    @JsonProperty("borrowerFeePerMonth")
    private BigDecimal borrowerFeePerMonth;

    @JsonProperty("interestPerMonth")
    private BigDecimal interestPerMonth;

    @JsonProperty("totalOutstandingAmount")
    private BigDecimal totalOutstandingAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("nextPaymentDate")
    private Date nextPaymentDate;

    @JsonProperty("nextPaymentAmount")
    private BigDecimal nextPaymentAmount;

    // TODO: include the repaymentSchedule
    @JsonProperty("repaymentSchedule")
    private List<Map<String,Object>> repaymentSchedule;

}
