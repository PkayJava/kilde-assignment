package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.scommon.dto.*;
import com.senior.kilde.assignment.dao.entity.*;
import com.senior.kilde.assignment.dao.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = InfoController.BASE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InfoController {

    public static final String BASE = "/info";
    public static final String INVESTOR = "/investor";
    public static final String BORROWER = "/borrower";
    public static final String ACCOUNT = "/account";
    public static final String REPAYMENT = "/repayment";
    public static final String TRANCHE = "/tranche";

    private final InvestorRepository investorRepository;

    private final AccountRepository accountRepository;

    private final BorrowerRepository borrowerRepository;

    private final BorrowerRepaymentRepository borrowerRepaymentRepository;

    private final TrancheRepository trancheRepository;

    @RequestMapping(path = TRANCHE + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoTrancheResponse> infoTranche(@PathVariable("id") String id) {
        Tranche tranche = this.trancheRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        InfoTrancheResponse response = new InfoTrancheResponse();
        response.setDuration(tranche.getDuration());
        response.setName(tranche.getName());
        response.setStatus(tranche.getStatus());
        response.setAnnualInterest(tranche.getAnnualInterest());
        response.setMaximumInvestmentAmount(tranche.getMaximumInvestmentAmount());
        response.setAmountAvailableForInvestment(tranche.getAmountAvailableForInvestment());
        response.setMinimumInvestmentAmount(tranche.getMinimumInvestmentAmount());

        return ResponseEntity.ok(response);
    }

    @RequestMapping(path = REPAYMENT + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoRepaymentResponse> infoRepayment(@PathVariable("id") String id) {
        BorrowerRepayment borrowerRepayment = this.borrowerRepaymentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        InfoRepaymentResponse response = new InfoRepaymentResponse();
        response.setCreatedDate(borrowerRepayment.getCreatedDate());
        response.setLoanAmount(borrowerRepayment.getLoanAmount());
        response.setLoanDuration(borrowerRepayment.getLoanDuration());
        response.setBorrowerFeePerMonth(borrowerRepayment.getBorrowerFeePerMonth());
        response.setInterestPerMonth(borrowerRepayment.getInterestPerMonth());
        response.setTotalOutstandingAmount(borrowerRepayment.getTotalOutstandingAmount());
        response.setNextPaymentDate(borrowerRepayment.getNextPaymentDate());
        response.setNextPaymentAmount(borrowerRepayment.getNextPaymentAmount());

        return ResponseEntity.ok(response);
    }

    @RequestMapping(path = INVESTOR + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoInvestorResponse> infoInvestor(@PathVariable("id") String id) {
        Investor investor = this.investorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Account account = this.accountRepository.findById(investor.getAccount().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        InfoInvestorResponse response = new InfoInvestorResponse();
        response.setName(investor.getName());
        response.setBalance(account.getBalance());
        response.setAccountNo(account.getAccountNo());

        return ResponseEntity.ok(response);
    }

    @RequestMapping(path = BORROWER + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoBorrowerResponse> infoBorrower(@PathVariable("id") String id) {
        Borrower borrower = this.borrowerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Account account = this.accountRepository.findById(borrower.getAccount().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        InfoBorrowerResponse response = new InfoBorrowerResponse();
        response.setName(borrower.getName());
        response.setBalance(account.getBalance());
        response.setAccountNo(account.getAccountNo());

        return ResponseEntity.ok(response);
    }

    @RequestMapping(path = ACCOUNT + "/{accountNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoAccountResponse> infoAccount(@PathVariable("accountNo") String accountNo) {
        Account account = this.accountRepository.findByAccountNo(accountNo).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        InfoAccountResponse response = new InfoAccountResponse();
        response.setBalance(account.getBalance());
        response.setAccountNo(account.getAccountNo());

        return ResponseEntity.ok(response);
    }

}
