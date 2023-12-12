package com.senior.kilde.assignment.dao.repository;

import com.senior.kilde.assignment.dao.entity.Account;
import com.senior.kilde.assignment.dao.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByAccountNo(String accountNo);

}
