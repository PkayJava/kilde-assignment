package com.senior.kilde.assignment.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrancheListResponse {

    private List<TrancheItemDto> items;

}
