package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.dto.InvestmentBorrowRequest;
import com.senior.kilde.assignment.api.dto.InvestmentBorrowResponse;
import com.senior.kilde.assignment.api.dto.InvestmentInvestRequest;
import com.senior.kilde.assignment.api.dto.InvestmentInvestResponse;
import com.senior.kilde.assignment.dao.entity.*;
import com.senior.kilde.assignment.dao.enums.TrancheStatus;
import com.senior.kilde.assignment.dao.repository.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.LocalDate;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(path = InvestmentController.BASE)
public class InvestmentController {

    public static final String BASE = "/investment";
    public static final String INVEST = "/invest";
    public static final String BORROW = "/borrow";

    private final InvestorRepository investorRepository;

    private final TrancheRepository trancheRepository;

    private final TrancheFundRepository trancheFundRepository;

    private final AccountRepository accountRepository;

    private final BorrowerRepository borrowerRepository;

    private final BorrowerRepaymentRepository borrowerRepaymentRepository;

    public InvestmentController(
            InvestorRepository investorRepository,
            TrancheRepository trancheRepository,
            TrancheFundRepository trancheFundRepository,
            BorrowerRepaymentRepository borrowerRepaymentRepository,
            BorrowerRepository borrowerRepository,
            AccountRepository accountRepository
    ) {
        this.investorRepository = investorRepository;
        this.trancheRepository = trancheRepository;
        this.trancheFundRepository = trancheFundRepository;
        this.accountRepository = accountRepository;
        this.borrowerRepaymentRepository = borrowerRepaymentRepository;
        this.borrowerRepository = borrowerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = INVEST, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestmentInvestResponse> investmentInvest(
            RequestEntity<InvestmentInvestRequest> httpRequest
    ) throws CloneNotSupportedException {
        InvestmentInvestRequest request = httpRequest.getBody();

        if (request.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required");
        }

        Optional<Tranche> optionalTranche = this.trancheRepository.findByName(request.getTrancheName());
        Tranche tranche = optionalTranche.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "trancheName is not found"));
        tranche = (Tranche) tranche.clone();

        if (tranche.getStatus() == TrancheStatus.InProgress) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, request.getTrancheName() + " already in progress");
        }

        Optional<Investor> optionalInvestor = this.investorRepository.findByName(request.getInvestorName());
        Investor investor = optionalInvestor.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "investorName is not found"));

        Account account = this.accountRepository.findById(investor.getAccount().getId()).orElseThrow();
        account = (Account) account.clone();
        if (account.getBalance().doubleValue() < request.getAmount().doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "insufficient balance");
        }

        BigDecimal limit = tranche.getMaximumInvestmentAmountPerInvestor();
        if (tranche.getAmountAvailableForInvestment().subtract(tranche.getMaximumInvestmentAmount()).doubleValue() > limit.doubleValue()) {
            limit = tranche.getAmountAvailableForInvestment().subtract(tranche.getMaximumInvestmentAmount());
        }

        if (request.getAmount().doubleValue() > limit.doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exceed maximum investment amount, currently available only " + limit);
        }

        if (request.getAmount().doubleValue() < tranche.getMinimumInvestmentAmount().doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exceed minimum investment amount, currently is at least " + tranche.getMinimumInvestmentAmount());
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        this.accountRepository.save(account);

        tranche.setMaximumInvestmentAmount(tranche.getMaximumInvestmentAmount().add(request.getAmount()));
        this.trancheRepository.save(tranche);

        TrancheFund trancheFund = new TrancheFund();
        trancheFund.setFundAmount(request.getAmount());
        trancheFund.setTranche(tranche);
        trancheFund.setInvestor(investor);
        this.trancheFundRepository.save(trancheFund);

        // TODO : response detail of that tranche
        InvestmentInvestResponse response = new InvestmentInvestResponse();
        response.setMessage("mess");

        return ResponseEntity.ok(response);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = BORROW, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestmentBorrowResponse> investmentBorrow(
            RequestEntity<InvestmentBorrowRequest> httpRequest
    ) throws CloneNotSupportedException {
        InvestmentBorrowRequest request = httpRequest.getBody();

        if (request.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required");
        }

        if (request.getDuration() == null || request.getDuration() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duration is required and must be positive");
        }

        Optional<Borrower> optionalBorrower = this.borrowerRepository.findByName(request.getBorrowerName());
        Borrower borrower = optionalBorrower.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "borrowerName is not found"));

        Optional<Tranche> optionalTranche = this.trancheRepository.findByName(request.getTrancheName());
        Tranche tranche = optionalTranche.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "trancheName is not found"));
        tranche = (Tranche) tranche.clone();
        if (tranche.getStatus() == TrancheStatus.InProgress) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, request.getTrancheName() + " already in progress");
        }

        BigDecimal amount = request.getAmount();
        if (amount.doubleValue() > tranche.getMaximumInvestmentAmount().doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, request.getTrancheName() + " insufficient fund, available only " + tranche.getMaximumInvestmentAmount());
        }

        if (request.getDuration() > tranche.getDuration()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duration must be between 1 to " + tranche.getDuration());
        }

        Account borrowerAccount = this.accountRepository.findById(borrower.getAccount().getId()).orElseThrow();
        borrowerAccount = (Account) borrowerAccount.clone();

        BigDecimal interestRatePerYear = BigDecimal.valueOf(tranche.getAnnualInterest() / 100f);
        BigDecimal interestRatePerMonth = interestRatePerYear.divide(BigDecimal.valueOf(12d));
        BigDecimal borrowerFeePerMonth = amount.multiply(BigDecimal.valueOf(0.02f));
        BigDecimal interestPerMonth = amount.multiply(interestRatePerMonth);

        tranche.setStatus(TrancheStatus.InProgress);
        this.trancheRepository.save(tranche);

        Account account = new Account();
        account.setAccountNo(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomNumeric(6));
        account.setBalance(BigDecimal.valueOf(0D));
        this.accountRepository.save(account);

        LocalDate today = LocalDate.now();

        BorrowerRepayment borrowerRepayment = new BorrowerRepayment();
        borrowerRepayment.setTranche(tranche);
        borrowerRepayment.setBorrower(borrower);
        borrowerRepayment.setAccount(account);
        borrowerRepayment.setLoanAmount(amount);
        borrowerRepayment.setLoanDuration(request.getDuration());
        borrowerRepayment.setMonthPaymentCount(0);
        borrowerRepayment.setOriginPaymentDay(today.getDayOfMonth());
        borrowerRepayment.setCreatedDate(today.toDate());
        borrowerRepayment.setBorrowerFeePerMonth(borrowerFeePerMonth);
        borrowerRepayment.setInterestPerMonth(interestPerMonth);
        borrowerRepayment.setTotalOutstandingAmount(borrowerFeePerMonth.add(interestPerMonth).multiply(BigDecimal.valueOf(request.getDuration())));
        borrowerRepayment.setTotalRepaymentAmount(BigDecimal.valueOf(0D));
        borrowerRepayment.setNextPaymentDate(today.plusMonths(1).toDate());
        borrowerRepayment.setNextPaymentAmount(interestPerMonth.add(borrowerFeePerMonth));

        this.borrowerRepaymentRepository.save(borrowerRepayment);

        borrowerAccount.setBalance(borrowerAccount.getBalance().add(amount));
        this.accountRepository.save(borrowerAccount);

        // TODO : create a borrow repayment
        InvestmentBorrowResponse response = new InvestmentBorrowResponse();

        return ResponseEntity.ok(response);
    }

}
