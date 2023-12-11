package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.dto.*;
import com.senior.kilde.assignment.dao.entity.Borrower;
import com.senior.kilde.assignment.dao.repository.BorrowerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = BorrowerController.BASE)
public class BorrowerController {

    public static final String BASE = "/borrower";
    public static final String LIST = "/list";
    public static final String CREATE = "/create";
    public static final String DETAIL = "/detail";
    public static final String UPDATE = "/update";

    private final BorrowerRepository borrowerRepository;

    public BorrowerController(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
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

        Borrower borrower = new Borrower();
        borrower.setName(request.getName());
        borrowerRepository.save(borrower);

        String currentPath = httpRequest.getUrl().toString();
        String basePath = currentPath.substring(0, currentPath.length() - CREATE.length());
        String detailPath = basePath + DETAIL + "/" + borrower.getId();

        BorrowerCreateResponse response = new BorrowerCreateResponse();
        response.setId(borrower.getId());
        response.setName(borrower.getName());
        response.setVersion(borrower.getVersion());

        return ResponseEntity.created(URI.create(detailPath)).body(response);
    }

    @RequestMapping(path = DETAIL + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowerDetailResponse> borrowerDetail(@PathVariable("id") String id) {
        Optional<Borrower> optionalBorrower = this.borrowerRepository.findById(id);
        Borrower borrower = optionalBorrower.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        BorrowerDetailResponse response = new BorrowerDetailResponse();
        response.setId(borrower.getId());
        response.setName(response.getName());
        response.setVersion(response.getVersion());

        return ResponseEntity.ok(response);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
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

        Optional<Borrower> optionalBorrower = this.borrowerRepository.findById(id);
        Borrower borrower = optionalBorrower.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        borrower = (Borrower) borrower.clone();
        borrower.setName(request.getName());
        borrower.setVersion(request.getVersion());
        this.borrowerRepository.save(borrower);

        BorrowerUpdateResponse response = new BorrowerUpdateResponse();
        response.setName(request.getName());
        response.setVersion(response.getVersion());

        return ResponseEntity.ok(response);
    }

}
