package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.dto.TrancheDto;
import com.senior.kilde.assignment.api.dto.TrancheItemDto;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.repository.TrancheRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/tranche")
public class TrancheController {

    private final TrancheRepository trancheRepository;

    public TrancheController(TrancheRepository trancheRepository) {
        this.trancheRepository = trancheRepository;
    }

    @RequestMapping(path = "/list")
    public ResponseEntity<List<TrancheItemDto>> borrowerList() {
        List<Tranche> tranches = this.trancheRepository.findAll();
        if (tranches == null) {
            return ResponseEntity.ok(null);
        } else {
            List<TrancheItemDto> body = tranches.stream().map(it -> {
                TrancheItemDto dto = new TrancheItemDto();
                dto.setId(it.getId());
                dto.setName(it.getName());
                dto.setDuration(it.getDuration());
                dto.setAnnualInterest(it.getAnnualInterest());
                return dto;
            }).toList();
            return ResponseEntity.ok(body);
        }
    }

    @RequestMapping(path = "/detail/{id}")
    public ResponseEntity<TrancheDto> borrowerList(@PathVariable("id") String id) {
        Optional<Tranche> optionalTranche = this.trancheRepository.findById(id);
        Tranche tranche = optionalTranche.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        TrancheDto dto = new TrancheDto();
        dto.setId(tranche.getId());
        dto.setName(tranche.getName());
        dto.setDuration(tranche.getDuration());
        dto.setAnnualInterest(tranche.getAnnualInterest());
        dto.setMaximumInvestmentAmount(tranche.getMaximumInvestmentAmount());
        dto.setMinimumInvestmentAmount(tranche.getMinimumInvestmentAmount());
        dto.setAmountAvailableForInvestment(tranche.getAmountAvailableForInvestment());
        dto.setMaximumInvestmentAmountPerInvestor(tranche.getMaximumInvestmentAmountPerInvestor());

        return ResponseEntity.ok(dto);
    }

}