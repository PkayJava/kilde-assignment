package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.api.BaseTest;
import com.senior.kilde.assignment.scommon.dto.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class InvestorControllerTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvestorControllerTest.class);

    @Test
    public void investorCreateTest() {
        InvestorCreateRequest request = new InvestorCreateRequest();
        request.setInitialBalanceAmount(BigDecimal.valueOf(1000));
        request.setName("Socheat KHAUV" + DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomAlphabetic(10));
        ResponseEntity<InvestorCreateResponse> response = this.client.exchange("/api" + InvestorController.BASE + InvestorController.CREATE, HttpMethod.POST, new HttpEntity<>(request), InvestorCreateResponse.class);
        Assertions.assertEquals(201, response.getStatusCode().value());
    }

    @Test
    public void investorUpdateTest() {
        InvestorCreateRequest createRequest = new InvestorCreateRequest();
        createRequest.setInitialBalanceAmount(BigDecimal.valueOf(10000));
        createRequest.setName("Socheat KHAUV" + DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomAlphabetic(10));
        ResponseEntity<InvestorCreateResponse> createResponse = this.client.exchange("/api" + InvestorController.BASE + InvestorController.CREATE, HttpMethod.POST, new HttpEntity<>(createRequest), InvestorCreateResponse.class);

        InvestorUpdateRequest request = new InvestorUpdateRequest();
        request.setVersion(createResponse.getBody().getVersion());
        request.setName("Socheat KHAUV" + DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomAlphabetic(10));
        ResponseEntity<InvestorUpdateResponse> response = this.client.exchange("/api" + InvestorController.BASE + InvestorController.UPDATE + "/" + createResponse.getBody().getId(), HttpMethod.PUT, new HttpEntity<>(request), InvestorUpdateResponse.class);
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    //    @Test
    public void investorDepositTest() {
        InvestorCreateRequest createRequest = new InvestorCreateRequest();
        createRequest.setInitialBalanceAmount(BigDecimal.valueOf(10000));
        createRequest.setName("Socheat KHAUV" + DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomAlphabetic(10));
        ResponseEntity<InvestorCreateResponse> createResponse = this.client.exchange("/api" + InvestorController.BASE + InvestorController.CREATE, HttpMethod.POST, new HttpEntity<>(createRequest), InvestorCreateResponse.class);

        InvestorDepositRequest request = new InvestorDepositRequest();
        request.setInvestorName(createResponse.getBody().getName());
        request.setNote("test deposit");
        request.setAmount(BigDecimal.valueOf(1000));
        ResponseEntity<InvestorDepositResponse> response = this.client.exchange("/api" + InvestorController.BASE + InvestorController.DEPOSIT, HttpMethod.POST, new HttpEntity<>(request), InvestorDepositResponse.class);
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

}
