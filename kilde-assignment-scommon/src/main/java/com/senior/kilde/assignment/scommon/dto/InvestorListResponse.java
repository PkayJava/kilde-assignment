package com.senior.kilde.assignment.scommon.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvestorListResponse {

    private List<InvestorItemDto> items;

}
