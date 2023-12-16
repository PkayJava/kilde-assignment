package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.BaseTest;
import com.senior.kilde.assignment.scommon.dto.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BorrowerControllerTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BorrowerControllerTest.class);

    @Test
    public void borrowerCreateTest() {
        BorrowerCreateRequest request = new BorrowerCreateRequest();
        request.setName("Socheat KHAUV" + DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomAlphabetic(10));
        ResponseEntity<BorrowerCreateResponse> response = this.client.exchange("/api" + BorrowerController.BASE + BorrowerController.CREATE, HttpMethod.POST, new HttpEntity<>(request), BorrowerCreateResponse.class);
        Assertions.assertEquals(201, response.getStatusCode().value());
    }

    @Test
    public void borrowerUpdateTest() {
        BorrowerCreateRequest createRequest = new BorrowerCreateRequest();
        createRequest.setName("Socheat KHAUV" + DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomAlphabetic(10));
        ResponseEntity<BorrowerCreateResponse> createResponse = this.client.exchange("/api" + BorrowerController.BASE + BorrowerController.CREATE, HttpMethod.POST, new HttpEntity<>(createRequest), BorrowerCreateResponse.class);

        BorrowerUpdateRequest request = new BorrowerUpdateRequest();
        request.setVersion(createResponse.getBody().getVersion());
        request.setName("Socheat KHAUV" + DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomAlphabetic(10));
        ResponseEntity<BorrowerUpdateResponse> response = this.client.exchange("/api" + BorrowerController.BASE + BorrowerController.UPDATE + "/" + createResponse.getBody().getId(), HttpMethod.PUT, new HttpEntity<>(request), BorrowerUpdateResponse.class);
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void borrowerDepositTest() {
        BorrowerCreateRequest createRequest = new BorrowerCreateRequest();
        createRequest.setName("Socheat KHAUV" + DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomAlphabetic(10));
        ResponseEntity<BorrowerCreateResponse> createResponse = this.client.exchange("/api" + BorrowerController.BASE + BorrowerController.CREATE, HttpMethod.POST, new HttpEntity<>(createRequest), BorrowerCreateResponse.class);

        BorrowerDepositRequest request = new BorrowerDepositRequest();
        request.setBorrowerName(createResponse.getBody().getName());
        request.setNote("test deposit");
        request.setAmount(BigDecimal.valueOf(1000));
        ResponseEntity<BorrowerDepositResponse> response = this.client.exchange("/api" + BorrowerController.BASE + BorrowerController.DEPOSIT, HttpMethod.POST, new HttpEntity<>(request), BorrowerDepositResponse.class);
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

}
