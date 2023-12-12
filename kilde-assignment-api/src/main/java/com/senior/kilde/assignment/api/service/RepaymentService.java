package com.senior.kilde.assignment.api.service;

import com.senior.kilde.assignment.dao.entity.Account;
import com.senior.kilde.assignment.dao.entity.BorrowerRepayment;
import com.senior.kilde.assignment.dao.repository.AccountRepository;
import com.senior.kilde.assignment.dao.repository.BorrowerRepaymentRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class RepaymentService {

    private final BorrowerRepaymentRepository borrowerRepaymentRepository;

    private final AccountRepository accountRepository;

    public RepaymentService(BorrowerRepaymentRepository borrowerRepaymentRepository, AccountRepository accountRepository) {
        this.borrowerRepaymentRepository = borrowerRepaymentRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void processRepayment(BorrowerRepayment repayment) throws CloneNotSupportedException {
        Account account = this.accountRepository.findById(repayment.getAccount().getId()).orElseThrow();
        account = (Account) account.clone();
        if (account.getBalance().doubleValue() >= repayment.getNextPaymentAmount().doubleValue()) {
            account.setBalance(account.getBalance().subtract(repayment.getNextPaymentAmount()));
            this.accountRepository.save(account);

            LocalDate currentPaymentDate = LocalDate.fromDateFields(repayment.getNextPaymentDate());
            int originPaymentDay = repayment.getOriginPaymentDay();
            if (currentPaymentDate.getDayOfMonth() != repayment.getOriginPaymentDay()) {
                LocalDate nextPaymentDate = null;
                while (nextPaymentDate == null) {
                    nextPaymentDate = new LocalDate(currentPaymentDate.getYear(), currentPaymentDate.getMonthOfYear() + 1, originPaymentDay);
                    originPaymentDay = originPaymentDay - 1;
                }
                repayment.setNextPaymentDate(nextPaymentDate.toDate());
            } else {
                LocalDate nextPaymentDate = currentPaymentDate.plusMonths(1);
                repayment.setNextPaymentDate(nextPaymentDate.toDate());
            }
            repayment.setMonthPaymentCount(repayment.getMonthPaymentCount() + 1);
            repayment.setTotalRepaymentAmount(repayment.getTotalRepaymentAmount().add(repayment.getNextPaymentAmount()));
            repayment.setTotalOutstandingAmount(repayment.getTotalOutstandingAmount().subtract(repayment.getNextPaymentAmount()));
            if (repayment.getMonthPaymentCount() == (repayment.getLoanDuration() - 1)) {
                // last month
                repayment.setNextPaymentAmount(repayment.getNextPaymentAmount().add(repayment.getLoanAmount()));
            }
            this.borrowerRepaymentRepository.save(repayment);
        } else {
            throw new RuntimeException("sufficient fund");
        }
    }

}
