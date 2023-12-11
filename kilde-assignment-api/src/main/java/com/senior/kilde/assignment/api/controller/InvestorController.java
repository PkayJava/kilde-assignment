package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.dto.*;
import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.repository.InvestorRepository;
import com.senior.kilde.assignment.dao.repository.InvestorRepository;
import com.senior.kilde.assignment.dao.repository.InvestorRepository;
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
@RequestMapping(path = InvestorController.BASE)
public class InvestorController {

    public static final String BASE = "/investor";
    public static final String LIST = "/list";
    public static final String CREATE = "/create";
    public static final String DETAIL = "/detail";
    public static final String UPDATE = "/update";

    private final InvestorRepository investorRepository;

    public InvestorController(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    @RequestMapping(path = LIST)
    public ResponseEntity<InvestorListResponse> investorList() {
        List<Investor> investors = this.investorRepository.findAll();
        if (investors == null) {
            return ResponseEntity.ok(null);
        } else {
            List<InvestorItemDto> items = investors.stream().map(it -> {
                InvestorItemDto dto = new InvestorItemDto();
                dto.setId(it.getId());
                dto.setName(it.getName());
                dto.setVersion(it.getVersion());
                return dto;
            }).toList();
            InvestorListResponse response = new InvestorListResponse();
            response.setItems(items);
            return ResponseEntity.ok(response);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = CREATE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestorCreateResponse> investorCreate(RequestEntity<InvestorCreateRequest> httpRequest) {
        InvestorCreateRequest request = httpRequest.getBody();
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        boolean exists = investorRepository.existsByName(request.getName());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name is not available");
        }

        Investor investor = new Investor();
        investor.setName(request.getName());
        investorRepository.save(investor);

        String currentPath = httpRequest.getUrl().toString();
        String basePath = currentPath.substring(0, currentPath.length() - CREATE.length());
        String detailPath = basePath + DETAIL + "/" + investor.getId();

        InvestorCreateResponse response = new InvestorCreateResponse();
        response.setId(investor.getId());
        response.setName(investor.getName());
        response.setVersion(investor.getVersion());

        return ResponseEntity.created(URI.create(detailPath)).body(response);
    }

    @RequestMapping(path = DETAIL + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestorDetailResponse> investorDetail(@PathVariable("id") String id) {
        Optional<Investor> optionalInvestor = this.investorRepository.findById(id);
        Investor investor = optionalInvestor.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        InvestorDetailResponse response = new InvestorDetailResponse();
        response.setId(investor.getId());
        response.setName(investor.getName());
        response.setVersion(investor.getVersion());

        return ResponseEntity.ok(response);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @RequestMapping(path = UPDATE + "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvestorUpdateResponse> investorUpdate(@PathVariable("id") String id, RequestEntity<InvestorUpdateRequest> httpRequest) throws CloneNotSupportedException {
        InvestorUpdateRequest request = httpRequest.getBody();

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        boolean exists = investorRepository.existsByIdNotAndName(id, request.getName());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name is not available");
        }

        Optional<Investor> optionalInvestor = this.investorRepository.findById(id);
        Investor investor = optionalInvestor.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        investor = (Investor) investor.clone();
        investor.setName(request.getName());
        investor.setVersion(request.getVersion());
        investor = this.investorRepository.save(investor);

        InvestorUpdateResponse response = new InvestorUpdateResponse();
        response.setName(request.getName());
        response.setVersion(investor.getVersion() + 1);

        return ResponseEntity.ok(response);
    }

}
