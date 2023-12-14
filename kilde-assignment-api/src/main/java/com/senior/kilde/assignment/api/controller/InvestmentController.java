package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.scommon.dto.InvestmentBorrowRequest;
import com.senior.kilde.assignment.scommon.dto.InvestmentBorrowResponse;
import com.senior.kilde.assignment.scommon.dto.InvestmentInvestRequest;
import com.senior.kilde.assignment.scommon.dto.InvestmentInvestResponse;
import com.senior.kilde.assignment.scommon.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = InvestmentController.BASE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentController {

    public static final String BASE = "/investment";
    public static final String INVEST = "/invest";
    public static final String BORROW = "/borrow";

    private final InvestmentService investmentService;

    /**
     * invest function, that will debit the amount from the investor account and credit into tranche account and 2% investor will apply
     * 2% of the amount go to Platform Account - 0000-00-00-00000
     * 98% of the amount go to tranche Account
     * @param httpRequest
     * @return
     * @throws CloneNotSupportedException
     */
    @RequestMapping(path = INVEST, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestmentInvestResponse> investmentInvest(
            RequestEntity<InvestmentInvestRequest> httpRequest
    ) throws CloneNotSupportedException {
        InvestmentInvestRequest request = httpRequest.getBody();

        if (request.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required");
        }

        InvestmentInvestResponse response = this.investmentService.investmentInvest(request);

        return ResponseEntity.ok(response);
    }

    /**
     * borrow function, the amount will credit to borrower account, and create a loan account for that for loan collector process,
     * trache status will turn into InProgress, this tranche will be made as available once the loan completed it cycle.
     * @param httpRequest
     * @return
     * @throws CloneNotSupportedException
     */
    @RequestMapping(path = BORROW, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestmentBorrowResponse> investmentBorrow(
            RequestEntity<InvestmentBorrowRequest> httpRequest
    ) throws CloneNotSupportedException {
        InvestmentBorrowRequest request = httpRequest.getBody();

        if (request.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required");
        }

        if (request.getDuration() == null || request.getDuration() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duration is required and must be positive");
        }

        InvestmentBorrowResponse response = this.investmentService.investmentBorrow(request);

        return ResponseEntity.ok(response);
    }

}
