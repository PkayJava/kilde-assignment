package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.scommon.dto.*;
import com.senior.kilde.assignment.dao.entity.*;
import com.senior.kilde.assignment.dao.repository.*;
import lombok.RequiredArgsConstructor;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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

    private final TrancheFundRepository trancheFundRepository;

    /**
     * tranche detail
     * @param id
     * @return
     */
    @RequestMapping(path = TRANCHE + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoTrancheResponse> infoTranche(@PathVariable("id") String id) {
        Tranche tranche = this.trancheRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        InfoTrancheResponse response = new InfoTrancheResponse();
        response.setDuration(tranche.getDuration());
        response.setName(tranche.getName());
        response.setStatus(tranche.getStatus());
        response.setAnnualInterest(tranche.getAnnualInterest());
        response.setMaximumInvestmentAmount(tranche.getMaximumInvestmentAmount());
        response.setMaximumInvestmentAmountPerInvestor(tranche.getMaximumInvestmentAmountPerInvestor());
        response.setAmountAvailableForInvestment(tranche.getAmountAvailableForInvestment());
        response.setMinimumInvestmentAmount(tranche.getMinimumInvestmentAmount());

        List<TrancheFund> funds = this.trancheFundRepository.findByTranche(tranche);
        List<InfoTrancheResponseItem> items = new ArrayList<>(funds.size());
        for (TrancheFund fund : funds) {
            Investor investor = this.investorRepository.findById(fund.getInvestor().getId()).orElseThrow();
            InfoTrancheResponseItem item = new InfoTrancheResponseItem();
            item.setInvestorName(investor.getName());
            item.setInvestedAmount(fund.getFundAmount());
            items.add(item);
        }
        response.setInvestors(items);

        return ResponseEntity.ok(response);
    }

    /**
     * repayment detail
     * @param id
     * @return
     */
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

        List<InfoRepaymentResponseItem> schedules = new ArrayList<>();
        LocalDate paymentDate = LocalDate.fromDateFields(borrowerRepayment.getCreatedDate());
        for (int i = 1; i <= borrowerRepayment.getLoanDuration(); i++) {
            InfoRepaymentResponseItem item = new InfoRepaymentResponseItem();
            if (paymentDate.getDayOfMonth() == borrowerRepayment.getOriginPaymentDay()) {
                paymentDate = paymentDate.plusMonths(1);
                item.setPaymentDate(paymentDate.toDate());
            } else {
                paymentDate = paymentDate.plusMonths(1);
                int day = borrowerRepayment.getOriginPaymentDay();
                while (true) {
                    try {
                        paymentDate = new LocalDate(paymentDate.getYear(), paymentDate.getMonthOfYear(), day);
                        break;
                    } catch (IllegalFieldValueException e) {
                        day = day - 1;
                    }
                }
                item.setPaymentDate(paymentDate.toDate());
            }
            item.setMonth(i);
            if (i == borrowerRepayment.getLoanDuration()) {
                item.setPaymentAmount(borrowerRepayment.getNextPaymentAmount().add(borrowerRepayment.getLoanAmount()));
            } else {
                item.setPaymentAmount(borrowerRepayment.getNextPaymentAmount());
            }
            schedules.add(item);
        }
        response.setRepaymentSchedule(schedules);

        return ResponseEntity.ok(response);
    }

    /**
     * investor account balance
     * @param id
     * @return
     */
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

    /**
     * borrower account balance
     * @param id
     * @return
     */
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

    /**
     * account balance
     * @param accountNo
     * @return
     */
    @RequestMapping(path = ACCOUNT + "/{accountNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoAccountResponse> infoAccount(@PathVariable("accountNo") String accountNo) {
        Account account = this.accountRepository.findByAccountNo(accountNo).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        InfoAccountResponse response = new InfoAccountResponse();
        response.setBalance(account.getBalance());
        response.setAccountNo(account.getAccountNo());

        return ResponseEntity.ok(response);
    }

}
