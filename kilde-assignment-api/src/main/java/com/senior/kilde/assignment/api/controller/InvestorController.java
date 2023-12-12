package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.dto.*;
import com.senior.kilde.assignment.dao.entity.Account;
import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.repository.AccountRepository;
import com.senior.kilde.assignment.dao.repository.InvestorRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = InvestorController.BASE)
public class InvestorController {

    public static final String BASE = "/investor";
    public static final String LIST = "/list";
    public static final String CREATE = "/create";
    public static final String UPDATE = "/update";
    public static final String DEPOSIT = "/deposit";

    private final InvestorRepository investorRepository;

    private final AccountRepository accountRepository;

    public InvestorController(
            InvestorRepository investorRepository,
            AccountRepository accountRepository
    ) {
        this.investorRepository = investorRepository;
        this.accountRepository = accountRepository;
    }

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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = CREATE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestorCreateResponse> investorCreate(RequestEntity<InvestorCreateRequest> httpRequest) {
        InvestorCreateRequest request = httpRequest.getBody();
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        if (request.getInitialBalanceAmount() == null || request.getInitialBalanceAmount().doubleValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "initialBalanceAmount is required or initialBalanceAmount is negative");
        }

        boolean exists = investorRepository.existsByName(request.getName());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name is not available");
        }

        Account account = new Account();
        account.setAccountNo(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + "-" + RandomStringUtils.randomNumeric(5));
        account.setBalance(request.getInitialBalanceAmount());
        this.accountRepository.save(account);

        Investor investor = new Investor();
        investor.setName(request.getName());
        investor.setAccount(account);
        investorRepository.save(investor);

        InvestorCreateResponse response = new InvestorCreateResponse();
        response.setId(investor.getId());
        response.setName(investor.getName());
        response.setAccountNo(account.getAccountNo());
        response.setBalance(account.getBalance());
        response.setVersion(investor.getVersion());

        return ResponseEntity.created(null).body(response);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = UPDATE + "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestorUpdateResponse> investorUpdate(@PathVariable("id") String id, RequestEntity<InvestorUpdateRequest> httpRequest) throws CloneNotSupportedException {
        InvestorUpdateRequest request = httpRequest.getBody();

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        boolean exists = investorRepository.existsByIdNotAndName(id, request.getName());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name is not available");
        }

        Optional<Investor> optionalInvestor = this.investorRepository.findById(id);
        Investor investor = optionalInvestor.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        investor = (Investor) investor.clone();
        investor.setName(request.getName());
        investor.setVersion(request.getVersion());
        investor = this.investorRepository.save(investor);

        InvestorUpdateResponse response = new InvestorUpdateResponse();
        response.setName(request.getName());
        response.setVersion(investor.getVersion() + 1);

        return ResponseEntity.ok(response);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = DEPOSIT, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestorDepositResponse> investorDeposit(RequestEntity<InvestorDepositRequest> httpRequest) throws CloneNotSupportedException {
        InvestorDepositRequest request = httpRequest.getBody();

        if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required or amount is not positive");
        }

        boolean exists = investorRepository.existsByName(request.getInvestorName());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "investorName is not found");
        }

        Optional<Investor> optionalInvestor = this.investorRepository.findByName(request.getInvestorName());
        Investor investor = optionalInvestor.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Account account = accountRepository.findById(investor.getAccount().getId()).orElseThrow();
        account = (Account) account.clone();
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        InvestorDepositResponse response = new InvestorDepositResponse();
        response.setAmount(account.getBalance());

        return ResponseEntity.ok(response);
    }

}
