package com.senior.kilde.assignment.api.controller;

import com.senior.kilde.assignment.scommon.dto.BorrowerCreateRequest;
import com.senior.kilde.assignment.scommon.dto.BorrowerCreateResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

import java.util.Date;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BorrowerControllerTest {

    @Autowired
    private TestRestTemplate client;

    @BeforeEach
    public void setup() {
        BasicAuthenticationInterceptor authenticationInterceptor = new BasicAuthenticationInterceptor("admin", "admin");
        client.getRestTemplate().getInterceptors().add(authenticationInterceptor);
    }

    @Test
    public void borrowerCreateTest() {
        BorrowerCreateRequest request = new BorrowerCreateRequest();
        request.setName("Socheat KHAUV" + DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + RandomStringUtils.randomAlphabetic(10));
        ResponseEntity<BorrowerCreateResponse> response = client.postForEntity("/api" + BorrowerController.BASE + BorrowerController.CREATE, request, BorrowerCreateResponse.class);
        Assertions.assertEquals(201, response.getStatusCode().value());
    }

}
