package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.scommon.dto.*;
import com.senior.kilde.assignment.scommon.service.BorrowerService;
import com.senior.kilde.assignment.dao.entity.Borrower;
import com.senior.kilde.assignment.dao.repository.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = BorrowerController.BASE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BorrowerController {

    public static final String BASE = "/borrower";
    public static final String LIST = "/list";
    public static final String CREATE = "/create";
    public static final String UPDATE = "/update";
    public static final String DEPOSIT = "/deposit";

    private final BorrowerRepository borrowerRepository;

    private final BorrowerService borrowerService;

    /**
     * CRUD function, list
     *
     * @return
     */
    @RequestMapping(path = LIST)
    public ResponseEntity<BorrowerListResponse> borrowerList() {
        List<Borrower> borrowers = this.borrowerRepository.findAll();
        if (borrowers == null) {
            return ResponseEntity.ok(null);
        } else {
            List<BorrowerItemDto> items = borrowers.stream().map(it -> {
                BorrowerItemDto dto = new BorrowerItemDto();
                dto.setId(it.getId());
                dto.setName(it.getName());
                dto.setVersion(it.getVersion());
                return dto;
            }).toList();
            BorrowerListResponse response = new BorrowerListResponse();
            response.setItems(items);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * CRUD, create borrower, it will also create account
     *
     * @param httpRequest
     * @return
     */
    @RequestMapping(path = CREATE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowerCreateResponse> borrowerCreate(RequestEntity<BorrowerCreateRequest> httpRequest) {
        BorrowerCreateRequest request = httpRequest.getBody();
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        boolean exists = borrowerRepository.existsByName(request.getName());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name is not available");
        }

        BorrowerCreateResponse response = borrowerService.borrowerCreate(request);

        return ResponseEntity.created(null).body(response);
    }

    /**
     * CRUD, update his name
     *
     * @param id
     * @param httpRequest
     * @return
     * @throws CloneNotSupportedException
     */
    @RequestMapping(path = UPDATE + "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowerUpdateResponse> borrowerUpdate(@PathVariable("id") String id, RequestEntity<BorrowerUpdateRequest> httpRequest) throws CloneNotSupportedException {
        BorrowerUpdateRequest request = httpRequest.getBody();

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        boolean exists = borrowerRepository.existsByIdNotAndName(id, request.getName());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name is not available");
        }

        BorrowerUpdateResponse response = this.borrowerService.borrowerUpdate(id, request);

        return ResponseEntity.ok(response);
    }


    /**
     * deposit money into his account
     *
     * @param httpRequest
     * @return
     * @throws CloneNotSupportedException
     */
    @RequestMapping(path = DEPOSIT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowerDepositResponse> borrowerDeposit(RequestEntity<BorrowerDepositRequest> httpRequest) throws CloneNotSupportedException {
        BorrowerDepositRequest request = httpRequest.getBody();

        if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required or amount is not positive");
        }

        boolean exists = borrowerRepository.existsByName(request.getBorrowerName());
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "borrowerName is not found");
        }

        BorrowerDepositResponse response = this.borrowerService.borrowerDeposit(request);

        return ResponseEntity.ok(response);
    }

}
