package com.senior.kilde.assignment.ddl.flyway;

import com.senior.kilde.assignment.ddl.LiquibaseMigration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class V105__BorrowerRepaymentTable extends LiquibaseMigration {

    @Override
    protected List<String> getXmlChecksum() {
        return List.of("V105__BorrowerRepaymentTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V105__BorrowerRepaymentTable.xml");
    }

}