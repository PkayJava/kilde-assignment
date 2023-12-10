package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.repository.InvestorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/investor")
public class InvestorController {

    private final InvestorRepository investorRepository;

    public InvestorController(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    @RequestMapping(path = "/list")
    public ResponseEntity<List<String>> borrowerList() {
        List<Investor> investors = this.investorRepository.findAll();
        if (investors == null) {
            return ResponseEntity.ok(null);
        } else {
            List<String> body = investors.stream().map(Investor::getName).toList();
            return ResponseEntity.ok(body);
        }
    }

}
