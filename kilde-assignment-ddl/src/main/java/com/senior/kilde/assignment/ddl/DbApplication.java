package com.senior.kilde.assignment.ddl;

//import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.security.Security;

@SpringBootApplication(
        // @formatter:off
        exclude = {
                LiquibaseAutoConfiguration.class,
                SecurityAutoConfiguration.class
        },
        scanBasePackages = {
                "com.senior.kilde.assignment.dao.repository",
        }
        // @formatter:on
)
//@EnableJpaRepositories(basePackages = {"com.senior.kilde.assignment.dao.repository"})
@EntityScan(basePackages = "com.senior.kilde.assignment.dao.entity")
public class DbApplication {

//    static {
//        if (Security.getProperty(BouncyCastleProvider.PROVIDER_NAME) == null) {
//            Security.addProvider(new BouncyCastleProvider());
//        }
//    }

    public static void main(String[] args) {
        SpringApplication.run(DbApplication.class, args);
    }

}
