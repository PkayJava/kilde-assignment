package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.dao.entity.Borrower;
import com.senior.kilde.assignment.dao.repository.BorrowerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/borrower")
public class BorrowerController {

    private final BorrowerRepository borrowerRepository;

    public BorrowerController(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    @RequestMapping(path = "/list")
    public ResponseEntity<List<Borrower>> borrowerList() {
        return ResponseEntity.ok(this.borrowerRepository.findAll());
    }

}
