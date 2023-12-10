package com.senior.kilde.assignment.ddl.flyway;

import com.senior.cyber.frmk.jdbc.query.InsertQuery;
import com.senior.kilde.assignment.dao.entity.Role;
import com.senior.kilde.assignment.ddl.LiquibaseMigration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class V001__RoleTable extends LiquibaseMigration {

    @Override
    protected List<String> getXmlChecksum() {
        return List.of("V001__RoleTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V001__RoleTable.xml");

        Map<String, String> roles = new LinkedHashMap<>();
        roles.put(Role.NAME_ROOT, Role.DESCRIPTION_ROOT);
        roles.put("Investor", "Investor");
        roles.put("Borrower", "Borrower");

        for (Map.Entry<String, String> role : roles.entrySet()) {
            InsertQuery insertQuery = new InsertQuery("tbl_role");
            insertQuery.addValue("role_id", ":role_id", Map.of("role_id", UUID.randomUUID().toString()));
            insertQuery.addValue("name", ":name", Map.of("name", role.getKey()));
            insertQuery.addValue("description", ":description", Map.of("description", role.getValue()));
            insertQuery.addValue("enabled", "true");
            named.update(insertQuery.toSQL(), insertQuery.toParam());
        }
    }

}