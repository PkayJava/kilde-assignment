package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.scommon.dto.TrancheCreateRequest;
import com.senior.kilde.assignment.scommon.dto.TrancheCreateResponse;
import com.senior.kilde.assignment.scommon.dto.TrancheItemDto;
import com.senior.kilde.assignment.scommon.dto.TrancheListResponse;
import com.senior.kilde.assignment.scommon.service.TrancheService;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.repository.TrancheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = TrancheController.BASE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrancheController {

    public static final String BASE = "/tranche";
    public static final String LIST = "/list";
    public static final String CREATE = "/create";
    public static final String UPDATE = "/update";

    private final TrancheRepository trancheRepository;

    private final TrancheService trancheService;

    @RequestMapping(path = "/list")
    public ResponseEntity<TrancheListResponse> trancheList() {
        List<Tranche> tranches = this.trancheRepository.findAll();
        if (tranches == null) {
            return ResponseEntity.ok(null);
        } else {
            List<TrancheItemDto> items = tranches.stream().map(it -> {
                TrancheItemDto dto = new TrancheItemDto();
                dto.setId(it.getId());
                dto.setName(it.getName());
                dto.setVersion(it.getVersion());
                return dto;
            }).toList();
            TrancheListResponse response = new TrancheListResponse();
            response.setItems(items);
            return ResponseEntity.ok(response);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = CREATE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrancheCreateResponse> trancheCreate(RequestEntity<TrancheCreateRequest> httpRequest) {
        TrancheCreateRequest request = httpRequest.getBody();
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        if (request.getDuration() == null || request.getDuration() < 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duration is required and at least 12 month");
        }

        if (request.getAnnualInterestRate() == null || request.getAnnualInterestRate() > 100 || request.getAnnualInterestRate() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "annualInterestRate is required and must be between 0 to 100");
        }

        if (request.getMinimumInvestmentAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minimumInvestmentAmount is required");
        }

        if (request.getAmountAvailableForInvestment() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amountAvailableForInvestment is required");
        }

        if (request.getMaximumInvestmentAmountPerInvestor() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "maximumInvestmentAmountPerInvestor is required");
        }

        if (request.getMinimumInvestmentAmount().doubleValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minimumInvestmentAmount must greater than or equal 0");
        }

        // TODO : some other validation need to be check
//        if (request.getMaximumInvestmentAmount() < request.getMinimumInvestmentAmount()){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "maximumInvestmentAmount must greater than or equal minimumInvestmentAmount");
//        }

        TrancheCreateResponse response = this.trancheService.trancheCreate(request);

        return ResponseEntity.created(null).body(response);
    }

}