package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.dto.InvestmentInvestRequest;
import com.senior.kilde.assignment.api.dto.InvestmentInvestResponse;
import com.senior.kilde.assignment.dao.entity.Account;
import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.entity.TrancheFund;
import com.senior.kilde.assignment.dao.repository.*;
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

    private final BorrowerRepaymentRepository borrowerRepaymentRepository;

    public InvestmentController(
            InvestorRepository investorRepository,
            TrancheRepository trancheRepository,
            TrancheFundRepository trancheFundRepository,
            BorrowerRepaymentRepository borrowerRepaymentRepository,
            AccountRepository accountRepository
    ) {
        this.investorRepository = investorRepository;
        this.trancheRepository = trancheRepository;
        this.trancheFundRepository = trancheFundRepository;
        this.accountRepository = accountRepository;
        this.borrowerRepaymentRepository = borrowerRepaymentRepository;
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

        Optional<Investor> optionalInvestor = this.investorRepository.findByName(request.getInvestorName());
        Investor investor = optionalInvestor.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "investorName is not found"));

        {
            boolean existed = this.trancheFundRepository.existsByTrancheAndInvestor(tranche, investor);
            if (existed) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, request.getInvestorName() + " already invested in " + request.getTrancheName());
            }
        }

        {
            boolean existed = this.borrowerRepaymentRepository.existsByTranche(tranche);
            if (existed) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, request.getTrancheName() + " already in progress or completed");
            }
        }

        Account account = this.accountRepository.findById(investor.getAccount().getId()).orElseThrow();
        account = (Account) account.clone();
        if (account.getBalance().doubleValue() < request.getAmount().doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "insufficient balance");
        }

        BigDecimal limit = tranche.getMaximumInvestmentAmountPerInvestor();
        if (tranche.getAmountAvailableForInvestment().doubleValue() - tranche.getMaximumInvestmentAmount().doubleValue() > limit.doubleValue()) {
            limit = BigDecimal.valueOf(tranche.getAmountAvailableForInvestment().doubleValue() - tranche.getMaximumInvestmentAmount().doubleValue());
        }

        if (request.getAmount().doubleValue() > limit.doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exceed maximum investment amount, currently available only " + limit);
        }

        if (request.getAmount().doubleValue() < tranche.getMinimumInvestmentAmount().doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exceed minimum investment amount, currently is at least " + tranche.getMinimumInvestmentAmount());
        }

        account.setBalance(BigDecimal.valueOf(account.getBalance().doubleValue() - request.getAmount().doubleValue()));
        this.accountRepository.save(account);

        tranche.setMaximumInvestmentAmount(BigDecimal.valueOf(tranche.getMaximumInvestmentAmount().doubleValue() + request.getAmount().doubleValue()));
        this.trancheRepository.save(tranche);

        TrancheFund trancheFund = new TrancheFund();
        trancheFund.setFundAmount(request.getAmount());
        trancheFund.setTranche(tranche);
        trancheFund.setInvestor(investor);
        this.trancheFundRepository.save(trancheFund);

        InvestmentInvestResponse response = new InvestmentInvestResponse();
        response.setMessage("mess");

        return ResponseEntity.ok(response);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = BORROW, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestmentInvestResponse> investmentBorrow(
            RequestEntity<InvestmentInvestRequest> httpRequest
    ) throws CloneNotSupportedException {
        // TODO : create a borrow repayment
        InvestmentInvestResponse response = new InvestmentInvestResponse();
        response.setMessage("mess");

        return ResponseEntity.ok(response);
    }

}
