package com.senior.kilde.assignment.scommon.service;

import com.senior.kilde.assignment.dao.entity.Account;
import com.senior.kilde.assignment.dao.entity.AccountTransaction;
import com.senior.kilde.assignment.dao.entity.BorrowerRepayment;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.enums.AccountTransactionType;
import com.senior.kilde.assignment.dao.enums.TrancheStatus;
import com.senior.kilde.assignment.dao.repository.AccountRepository;
import com.senior.kilde.assignment.dao.repository.AccountTransactionRepository;
import com.senior.kilde.assignment.dao.repository.BorrowerRepaymentRepository;
import com.senior.kilde.assignment.dao.repository.TrancheRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RepaymentService {

    private final BorrowerRepaymentRepository borrowerRepaymentRepository;

    private final AccountRepository accountRepository;

    private final TrancheRepository trancheRepository;

    private final AccountTransactionRepository accountTransactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void processRepayment(BorrowerRepayment repayment) throws CloneNotSupportedException {
        Account account = this.accountRepository.findById(repayment.getAccount().getId()).orElseThrow();
        account = (Account) account.clone();
        if (account.getBalance().doubleValue() >= repayment.getNextPaymentAmount().doubleValue()) {
            account.setBalance(account.getBalance().subtract(repayment.getNextPaymentAmount()));
            this.accountRepository.save(account);

            AccountTransaction transaction = new AccountTransaction();
            transaction.setType(AccountTransactionType.DEBIT);
            transaction.setNote("Loan Repayment");
            transaction.setAmount(repayment.getNextPaymentAmount());
            transaction.setCreatedDate(new Date());
            transaction.setAccount(account);
            accountTransactionRepository.save(transaction);

            LocalDate currentPaymentDate = LocalDate.fromDateFields(repayment.getNextPaymentDate());
            int originPaymentDay = repayment.getOriginPaymentDay();
            if (currentPaymentDate.getDayOfMonth() != repayment.getOriginPaymentDay()) {
                LocalDate nextPaymentDate = null;
                while (true) {
                    try {
                        nextPaymentDate = new LocalDate(currentPaymentDate.getYear(), currentPaymentDate.getMonthOfYear() + 1, originPaymentDay);
                        break;
                    } catch (IllegalFieldValueException e) {
                        originPaymentDay = originPaymentDay - 1;
                    }
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
            } else if (repayment.getMonthPaymentCount().equals(repayment.getLoanDuration())) {
                repayment.setNextPaymentAmount(null);
                repayment.setNextPaymentDate(null);
                Tranche tranche = trancheRepository.findById(repayment.getTranche().getId()).orElseThrow();
                tranche = (Tranche) tranche.clone();
                tranche.setStatus(TrancheStatus.Available);
                trancheRepository.save(tranche);
            }
            this.borrowerRepaymentRepository.save(repayment);
        } else {
            throw new RuntimeException("sufficient fund");
        }
    }

}
