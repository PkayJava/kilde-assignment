package com.senior.kilde.assignment.dao.repository;

import com.senior.kilde.assignment.dao.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, String> {

    Optional<Borrower> findByName(String name);

    boolean existsByName(String name);

    boolean existsByIdNotAndName(String id, String name);

}
