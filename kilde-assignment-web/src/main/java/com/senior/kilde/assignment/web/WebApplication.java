package com.senior.kilde.assignment.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        // @formatter:off
        exclude = {
                LiquibaseAutoConfiguration.class,
                FlywayAutoConfiguration.class,
                SecurityAutoConfiguration.class
        },
        scanBasePackages = {
                "com.senior.kilde.assignment.dao.repository",
                "com.senior.kilde.assignment.web",
        }
        // @formatter:on
)
@EnableJpaRepositories(basePackages = {"com.senior.kilde.assignment.dao.repository"})
@EntityScan(basePackages = "com.senior.kilde.assignment.dao.entity")
public class WebApplication {

    // for static access in Apache Wicket
    private static ApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(WebApplication.class, args);
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

}
