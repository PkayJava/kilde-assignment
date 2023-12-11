package com.senior.kilde.assignment.dao.repository;

import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.entity.TrancheFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrancheFundRepository extends JpaRepository<TrancheFund, String> {

    boolean existsByTrancheAndInvestor(Tranche tranche, Investor investor);

}
