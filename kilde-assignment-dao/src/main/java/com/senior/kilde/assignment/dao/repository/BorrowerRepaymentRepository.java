package com.senior.kilde.assignment.dao.repository;

import com.senior.kilde.assignment.dao.entity.BorrowerRepayment;
import com.senior.kilde.assignment.dao.entity.Tranche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepaymentRepository extends JpaRepository<BorrowerRepayment, String> {

    boolean existsByTranche(Tranche tranche);

}
