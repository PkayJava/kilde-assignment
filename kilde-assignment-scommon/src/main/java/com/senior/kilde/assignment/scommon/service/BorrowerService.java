package com.senior.kilde.assignment.scommon.service;

import com.senior.kilde.assignment.dao.entity.Account;
import com.senior.kilde.assignment.dao.entity.AccountTransaction;
import com.senior.kilde.assignment.dao.entity.Borrower;
import com.senior.kilde.assignment.dao.enums.AccountTransactionType;
import com.senior.kilde.assignment.dao.repository.AccountRepository;
import com.senior.kilde.assignment.dao.repository.AccountTransactionRepository;
import com.senior.kilde.assignment.dao.repository.BorrowerRepository;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    private final AccountRepository accountRepository;

    private final AccountTransactionRepository accountTransactionRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public BorrowerCreateResponse borrowerCreate(BorrowerCreateRequest request) {
        Account account = new Account();
        account.setAccountNo(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + "-" + RandomStringUtils.randomNumeric(5));
        account.setBalance(BigDecimal.valueOf(0D));
        this.accountRepository.save(account);

        Borrower borrower = new Borrower();
        borrower.setAccount(account);
        borrower.setName(request.getName());
        this.borrowerRepository.save(borrower);

        BorrowerCreateResponse response = new BorrowerCreateResponse();
        response.setId(borrower.getId());
        response.setName(borrower.getName());
        response.setAccountNo(account.getAccountNo());
        response.setBalance(0D);
        response.setVersion(borrower.getVersion());

        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public BorrowerUpdateResponse borrowerUpdate(String id, BorrowerUpdateRequest request) throws CloneNotSupportedException {
        Optional<Borrower> optionalBorrower = this.borrowerRepository.findById(id);
        Borrower borrower = optionalBorrower.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        borrower = (Borrower) borrower.clone();
        borrower.setName(request.getName());
        borrower.setVersion(request.getVersion());

        this.borrowerRepository.save(borrower);

        BorrowerUpdateResponse response = new BorrowerUpdateResponse();
        response.setName(request.getName());
        response.setVersion(borrower.getVersion() + 1);

        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public BorrowerDepositResponse borrowerDeposit(BorrowerDepositRequest request) throws CloneNotSupportedException {
        Optional<Borrower> optionalBorrower = this.borrowerRepository.findByName(request.getBorrowerName());
        Borrower borrower = optionalBorrower.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Account account = accountRepository.findById(borrower.getAccount().getId()).orElseThrow();
        account = (Account) account.clone();
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        AccountTransaction transaction = new AccountTransaction();
        transaction.setType(AccountTransactionType.CREDIT);
        transaction.setNote(request.getNote());
        transaction.setAmount(request.getAmount());
        transaction.setCreatedDate(new Date());
        transaction.setAccount(account);
        accountTransactionRepository.save(transaction);

        BorrowerDepositResponse response = new BorrowerDepositResponse();
        response.setBalance(account.getBalance());

        return response;
    }

}
