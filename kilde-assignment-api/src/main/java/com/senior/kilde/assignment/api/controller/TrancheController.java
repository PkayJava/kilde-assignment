package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.dto.*;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.repository.TrancheRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = TrancheController.BASE)
public class TrancheController {

    public static final String BASE = "/tranche";
    public static final String LIST = "/list";
    public static final String CREATE = "/create";
    public static final String DETAIL = "/detail";
    public static final String UPDATE = "/update";

    private final TrancheRepository trancheRepository;

    public TrancheController(TrancheRepository trancheRepository) {
        this.trancheRepository = trancheRepository;
    }

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

        if (request.getMinimumInvestmentAmount() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minimumInvestmentAmount must greater than or equal 0");
        }

        // TODO : some other validation need to be check
//        if (request.getMaximumInvestmentAmount() < request.getMinimumInvestmentAmount()){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "maximumInvestmentAmount must greater than or equal minimumInvestmentAmount");
//        }

        boolean exists = trancheRepository.existsByName(request.getName());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name is not available");
        }

        Tranche tranche = new Tranche();
        tranche.setName(request.getName());
        tranche.setAnnualInterest(request.getAnnualInterestRate());
        tranche.setAmountAvailableForInvestment(request.getAmountAvailableForInvestment());
        tranche.setDuration(request.getDuration());
        tranche.setMinimumInvestmentAmount(request.getMinimumInvestmentAmount());
        tranche.setMaximumInvestmentAmount(0D);
        tranche.setMaximumInvestmentAmountPerInvestor(request.getMaximumInvestmentAmountPerInvestor());

        this.trancheRepository.save(tranche);

        String currentPath = httpRequest.getUrl().toString();
        String basePath = currentPath.substring(0, currentPath.length() - CREATE.length());
        String detailPath = basePath + DETAIL + "/" + tranche.getId();

        TrancheCreateResponse response = new TrancheCreateResponse();
        response.setId(tranche.getId());
        response.setName(tranche.getName());
        response.setVersion(tranche.getVersion());

        return ResponseEntity.created(URI.create(detailPath)).body(response);
    }

    @RequestMapping(path = DETAIL + "/{id}")
    public ResponseEntity<TrancheDetailResponse> trancheDetail(@PathVariable("id") String id) {
        Optional<Tranche> optionalTranche = this.trancheRepository.findById(id);
        Tranche tranche = optionalTranche.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        TrancheDetailResponse dto = new TrancheDetailResponse();
        dto.setId(tranche.getId());
        dto.setName(tranche.getName());
        dto.setDuration(tranche.getDuration());
        dto.setAnnualInterest(tranche.getAnnualInterest());
        dto.setMaximumInvestmentAmount(tranche.getMaximumInvestmentAmount());
        dto.setMinimumInvestmentAmount(tranche.getMinimumInvestmentAmount());
        dto.setAmountAvailableForInvestment(tranche.getAmountAvailableForInvestment());
        dto.setMaximumInvestmentAmountPerInvestor(tranche.getMaximumInvestmentAmountPerInvestor());
        dto.setVersion(tranche.getVersion());

        return ResponseEntity.ok(dto);
    }

}