package com.senior.kilde.assignment.scommon.service;

import com.senior.kilde.assignment.dao.entity.Account;
import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.repository.AccountRepository;
import com.senior.kilde.assignment.dao.repository.InvestorRepository;
import com.senior.kilde.assignment.scommon.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestorService {

    private final InvestorRepository investorRepository;

    private final AccountRepository accountRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public InvestorCreateResponse investorCreate(InvestorCreateRequest request) {

        boolean exists = this.investorRepository.existsByName(request.getName());
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
        this.investorRepository.save(investor);

        InvestorCreateResponse response = new InvestorCreateResponse();
        response.setId(investor.getId());
        response.setName(investor.getName());
        response.setAccountNo(account.getAccountNo());
        response.setBalance(account.getBalance());
        response.setVersion(investor.getVersion());

        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public InvestorUpdateResponse investorUpdate(String id, InvestorUpdateRequest request) throws CloneNotSupportedException {
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

        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public InvestorDepositResponse investorDeposit(InvestorDepositRequest request) throws CloneNotSupportedException {
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

        return response;
    }

}
