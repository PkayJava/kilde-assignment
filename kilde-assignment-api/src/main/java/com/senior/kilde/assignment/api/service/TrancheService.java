package com.senior.kilde.assignment.api.service;

import com.senior.kilde.assignment.api.dto.TrancheCreateRequest;
import com.senior.kilde.assignment.api.dto.TrancheCreateResponse;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.enums.TrancheStatus;
import com.senior.kilde.assignment.dao.repository.TrancheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrancheService {

    private final TrancheRepository trancheRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public TrancheCreateResponse trancheCreate(TrancheCreateRequest request) {
        boolean exists = this.trancheRepository.existsByName(request.getName());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name is not available");
        }

        Tranche tranche = new Tranche();
        tranche.setName(request.getName());
        tranche.setAnnualInterest(request.getAnnualInterestRate());
        tranche.setAmountAvailableForInvestment(request.getAmountAvailableForInvestment());
        tranche.setDuration(request.getDuration());
        tranche.setMinimumInvestmentAmount(request.getMinimumInvestmentAmount());
        tranche.setMaximumInvestmentAmount(BigDecimal.valueOf(0D));
        tranche.setMaximumInvestmentAmountPerInvestor(request.getMaximumInvestmentAmountPerInvestor());
        tranche.setStatus(TrancheStatus.Available);

        this.trancheRepository.save(tranche);

        TrancheCreateResponse response = new TrancheCreateResponse();
        response.setId(tranche.getId());
        response.setName(tranche.getName());
        response.setVersion(tranche.getVersion());

        return response;
    }

}
