package com.senior.kilde.assignment.ddl.flyway;

import com.senior.kilde.assignment.ddl.LiquibaseMigration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class V006__GroupRoleTable extends LiquibaseMigration {

    @Override
    protected List<String> getXmlChecksum() {
        return List.of("V006__GroupRoleTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V006__GroupRoleTable.xml");
    }

}