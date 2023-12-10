package com.senior.kilde.assignment.dao.repository;

import com.senior.kilde.assignment.dao.entity.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, String> {

    Optional<Investor> findByName(String name);

}
