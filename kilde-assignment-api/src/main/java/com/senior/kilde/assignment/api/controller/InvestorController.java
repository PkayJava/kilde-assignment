package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.dto.*;
import com.senior.kilde.assignment.api.service.InvestorService;
import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.repository.AccountRepository;
import com.senior.kilde.assignment.dao.repository.InvestorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = InvestorController.BASE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestorController {

    public static final String BASE = "/investor";
    public static final String LIST = "/list";
    public static final String CREATE = "/create";
    public static final String UPDATE = "/update";
    public static final String DEPOSIT = "/deposit";

    private final InvestorRepository investorRepository;

    private final AccountRepository accountRepository;

    private final InvestorService investorService;

    @RequestMapping(path = LIST)
    public ResponseEntity<InvestorListResponse> investorList() {
        List<Investor> investors = this.investorRepository.findAll();
        if (investors == null) {
            return ResponseEntity.ok(null);
        } else {
            List<InvestorItemDto> items = investors.stream().map(it -> {
                InvestorItemDto dto = new InvestorItemDto();
                dto.setId(it.getId());
                dto.setName(it.getName());
                dto.setVersion(it.getVersion());
                return dto;
            }).toList();
            InvestorListResponse response = new InvestorListResponse();
            response.setItems(items);
            return ResponseEntity.ok(response);
        }
    }

    @RequestMapping(path = CREATE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestorCreateResponse> investorCreate(RequestEntity<InvestorCreateRequest> httpRequest) {
        InvestorCreateRequest request = httpRequest.getBody();
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        if (request.getInitialBalanceAmount() == null || request.getInitialBalanceAmount().doubleValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "initialBalanceAmount is required or initialBalanceAmount is negative");
        }

        InvestorCreateResponse response = this.investorService.investorCreate(request);

        return ResponseEntity.created(null).body(response);
    }

    @RequestMapping(path = UPDATE + "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestorUpdateResponse> investorUpdate(@PathVariable("id") String id, RequestEntity<InvestorUpdateRequest> httpRequest) throws CloneNotSupportedException {
        InvestorUpdateRequest request = httpRequest.getBody();

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        InvestorUpdateResponse response = this.investorService.investorUpdate(id, request);

        return ResponseEntity.ok(response);
    }

    @RequestMapping(path = DEPOSIT, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestorDepositResponse> investorDeposit(RequestEntity<InvestorDepositRequest> httpRequest) throws CloneNotSupportedException {
        InvestorDepositRequest request = httpRequest.getBody();

        if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required or amount is not positive");
        }

        InvestorDepositResponse response = this.investorService.investorDeposit(request);

        return ResponseEntity.ok(response);
    }

}
