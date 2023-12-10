package com.senior.kilde.assignment.ddl.flyway;

import com.senior.cyber.frmk.jdbc.query.InsertQuery;
import com.senior.kilde.assignment.ddl.LiquibaseMigration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class V002__GroupTable extends LiquibaseMigration {

    @Override
    protected List<String> getXmlChecksum() {
        return List.of("V002__GroupTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V002__GroupTable.xml");
        InsertQuery insertQuery = new InsertQuery("tbl_group");
        insertQuery.addValue("group_id", ":group_id", Map.of("group_id", UUID.randomUUID().toString()));
        insertQuery.addValue("name", ":name", Map.of("name", "Registered"));
        insertQuery.addValue("enabled", "true");
        named.update(insertQuery.toSQL(), insertQuery.toParam());

    }

}