package com.senior.kilde.assignment.dao.repository;

import com.senior.kilde.assignment.dao.entity.Tranche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrancheRepository extends JpaRepository<Tranche, String> {

    Optional<Tranche> findByName(String name);

    boolean existsByName(String name);

    boolean existsByIdNotAndName(String id, String name);

}
