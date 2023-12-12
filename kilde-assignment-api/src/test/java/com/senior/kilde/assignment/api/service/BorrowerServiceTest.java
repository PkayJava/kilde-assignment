package com.senior.kilde.assignment.api.service;

import com.senior.kilde.assignment.scommon.dto.BorrowerCreateRequest;
import com.senior.kilde.assignment.scommon.dto.BorrowerCreateResponse;
import com.senior.kilde.assignment.scommon.service.BorrowerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class BorrowerServiceTest {

    @Autowired
    private BorrowerService borrowerService;

    @Test
    public void borrowerCreateTest() {
        BorrowerCreateRequest request = new BorrowerCreateRequest();
        request.setName(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()) + "-" + RandomStringUtils.randomAlphabetic(10));
        BorrowerCreateResponse response = this.borrowerService.borrowerCreate(request);
        Assertions.assertNotNull(response);
    }

}