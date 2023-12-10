package com.senior.kilde.assignment.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        // @formatter:off
        exclude = {
                LiquibaseAutoConfiguration.class,
                FlywayAutoConfiguration.class
        },
        scanBasePackages = {
                "com.senior.kilde.assignment.dao.repository",
                "com.senior.kilde.assignment.api"
        }
        // @formatter:on
)
@EnableJpaRepositories(basePackages = {"com.senior.kilde.assignment.dao.repository"})
@EntityScan(basePackages = "com.senior.kilde.assignment.dao.entity")
public class ApiApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApiApplication.class, args);
    }

}
