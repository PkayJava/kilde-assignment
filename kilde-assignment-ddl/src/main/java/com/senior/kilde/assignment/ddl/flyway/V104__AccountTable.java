package com.senior.kilde.assignment.ddl.flyway;

import com.senior.cyber.frmk.jdbc.query.InsertQuery;
import com.senior.kilde.assignment.ddl.LiquibaseMigration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class V104__AccountTable extends LiquibaseMigration {

    @Override
    protected List<String> getXmlChecksum() {
        return List.of("V104__AccountTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V104__AccountTable.xml");

        InsertQuery insertQuery = new InsertQuery("tbl_account");
        insertQuery.addValue("account_id", ":account_id", Map.of("account_id", UUID.randomUUID().toString()));
        insertQuery.addValue("account_no", ":account_no", Map.of("account_no", "0000-00-00-00000"));
        insertQuery.addValue("balance", ":balance", Map.of("balance", 0D));
        insertQuery.addValue("version", "1");
        named.update(insertQuery.toSQL(), insertQuery.toParam());
    }

}