package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.dto.InvestmentInvestRequest;
import com.senior.kilde.assignment.api.dto.InvestmentInvestResponse;
import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.entity.TrancheFund;
import com.senior.kilde.assignment.dao.repository.InvestorRepository;
import com.senior.kilde.assignment.dao.repository.TrancheFundRepository;
import com.senior.kilde.assignment.dao.repository.TrancheRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(path = InvestmentController.BASE)
public class InvestmentController {

    public static final String BASE = "/investment";
    public static final String INVEST = "/invest";

    private final InvestorRepository investorRepository;

    private final TrancheRepository trancheRepository;

    private final TrancheFundRepository trancheFundRepository;

    public InvestmentController(
            InvestorRepository investorRepository,
            TrancheRepository trancheRepository,
            TrancheFundRepository trancheFundRepository
    ) {
        this.investorRepository = investorRepository;
        this.trancheRepository = trancheRepository;
        this.trancheFundRepository = trancheFundRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = INVEST, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestmentInvestResponse> investmentInvest(
            RequestEntity<InvestmentInvestRequest> httpRequest
    ) throws CloneNotSupportedException {
        InvestmentInvestRequest request = httpRequest.getBody();

        if (request.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required");
        }

        Optional<Tranche> optionalTranche = this.trancheRepository.findByName(request.getTrancheName());
        Tranche tranche = optionalTranche.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "trancheName is not found"));
        tranche = (Tranche) tranche.clone();

        Optional<Investor> optionalInvestor = this.investorRepository.findByName(request.getInvestorName());
        Investor investor = optionalInvestor.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "investorName is not found"));

        boolean existed = this.trancheFundRepository.existsByTrancheAndInvestor(tranche, investor);
        if (existed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, request.getInvestorName() + " already invested in " + request.getTrancheName());
        }

        Double limit = tranche.getMaximumInvestmentAmountPerInvestor();
        if (tranche.getAmountAvailableForInvestment() - tranche.getMaximumInvestmentAmount() > limit) {
            limit = tranche.getAmountAvailableForInvestment() - tranche.getMaximumInvestmentAmount();
        }

        if (request.getAmount() > limit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exceed maximum investment amount, currently available only " + limit);
        }

        if (request.getAmount() < tranche.getMinimumInvestmentAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exceed minimum investment amount, currently is at least " + tranche.getMinimumInvestmentAmount());
        }

        tranche.setMaximumInvestmentAmount(tranche.getMaximumInvestmentAmount() + request.getAmount());
        this.trancheRepository.save(tranche);

        TrancheFund trancheFund = new TrancheFund();
        trancheFund.setFundAmount(request.getAmount());
        trancheFund.setTranche(tranche);
        trancheFund.setInvestor(investor);
        this.trancheFundRepository.save(trancheFund);

        InvestmentInvestResponse response = new InvestmentInvestResponse();
        response.setMessage("mess");

        return ResponseEntity.ok(response);
    }

}
