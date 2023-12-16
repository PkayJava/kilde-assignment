package com.senior.kilde.assignment.api;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

public class BaseTest {

    @Autowired
    protected TestRestTemplate client;

    @BeforeEach
    public void setup() {
        BasicAuthenticationInterceptor authenticationInterceptor = new BasicAuthenticationInterceptor("admin", "admin");
        this.client.getRestTemplate().getInterceptors().add(authenticationInterceptor);
    }

}
