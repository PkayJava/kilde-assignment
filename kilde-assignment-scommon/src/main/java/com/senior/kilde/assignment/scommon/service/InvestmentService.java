package com.senior.kilde.assignment.scommon.service;

import com.senior.kilde.assignment.dao.entity.*;
import com.senior.kilde.assignment.dao.enums.AccountTransactionType;
import com.senior.kilde.assignment.dao.enums.TrancheStatus;
import com.senior.kilde.assignment.dao.repository.*;
import com.senior.kilde.assignment.scommon.dto.InvestmentBorrowRequest;
import com.senior.kilde.assignment.scommon.dto.InvestmentBorrowResponse;
import com.senior.kilde.assignment.scommon.dto.InvestmentInvestRequest;
import com.senior.kilde.assignment.scommon.dto.InvestmentInvestResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentService {

    public static final String PLATFORM_ACCOUNT_NO = "0000-00-00-00000";

    private final TrancheRepository trancheRepository;

    private final InvestorRepository investorRepository;

    private final AccountRepository accountRepository;

    private final AccountTransactionRepository accountTransactionRepository;

    private final TrancheFundRepository trancheFundRepository;

    private final BorrowerRepository borrowerRepository;

    private final BorrowerRepaymentRepository borrowerRepaymentRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public InvestmentInvestResponse investmentInvest(InvestmentInvestRequest request) throws CloneNotSupportedException {

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

        {
            account.setBalance(account.getBalance().subtract(request.getAmount()));
            this.accountRepository.save(account);
            AccountTransaction transaction = new AccountTransaction();
            transaction.setType(AccountTransactionType.DEBIT);
            transaction.setNote("Investment");
            transaction.setAmount(request.getAmount());
            transaction.setCreatedDate(new Date());
            transaction.setAccount(account);
            accountTransactionRepository.save(transaction);
        }

        BigDecimal investorFee = request.getAmount().multiply(BigDecimal.valueOf(0.02d));
        BigDecimal investmentAmount = request.getAmount().subtract(investorFee);

        tranche.setMaximumInvestmentAmount(tranche.getMaximumInvestmentAmount().add(investmentAmount));
        this.trancheRepository.save(tranche);

        TrancheFund trancheFund = new TrancheFund();
        trancheFund.setFundAmount(request.getAmount());
        trancheFund.setTranche(tranche);
        trancheFund.setInvestor(investor);
        this.trancheFundRepository.save(trancheFund);

        {
            Account platformAccount = this.accountRepository.findByAccountNo(PLATFORM_ACCOUNT_NO).orElseThrow();
            platformAccount.setBalance(platformAccount.getBalance().add(investorFee));
            AccountTransaction transaction = new AccountTransaction();
            transaction.setType(AccountTransactionType.CREDIT);
            transaction.setNote("Platform Fee");
            transaction.setAmount(request.getAmount());
            transaction.setCreatedDate(new Date());
            transaction.setAccount(platformAccount);
            accountTransactionRepository.save(transaction);
        }

        InvestmentInvestResponse response = new InvestmentInvestResponse();
        response.setMessage("ok");

        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public InvestmentBorrowResponse investmentBorrow(InvestmentBorrowRequest request) throws CloneNotSupportedException {
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
        BigDecimal interestRatePerMonth = interestRatePerYear.divide(BigDecimal.valueOf(12d), 4, RoundingMode.HALF_UP);
        BigDecimal borrowerFeePerMonth = amount.multiply(BigDecimal.valueOf(0.02f));
        BigDecimal interestPerMonth = amount.multiply(interestRatePerMonth);

        tranche.setStatus(TrancheStatus.InProgress);
        this.trancheRepository.save(tranche);

        Account account = new Account();
        account.setAccountNo(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + "-" + RandomStringUtils.randomNumeric(5));
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

        AccountTransaction transaction = new AccountTransaction();
        transaction.setType(AccountTransactionType.CREDIT);
        transaction.setNote("Loan Amount");
        transaction.setAmount(request.getAmount());
        transaction.setCreatedDate(new Date());
        transaction.setAccount(borrowerAccount);
        accountTransactionRepository.save(transaction);

        InvestmentBorrowResponse response = new InvestmentBorrowResponse();
        response.setMessage("ok");

        return response;
    }

}
