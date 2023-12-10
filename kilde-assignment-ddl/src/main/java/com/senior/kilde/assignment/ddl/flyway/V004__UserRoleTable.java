package com.senior.kilde.assignment.ddl.flyway;

import com.senior.cyber.frmk.jdbc.query.InsertQuery;
import com.senior.kilde.assignment.dao.entity.Role;
import com.senior.kilde.assignment.ddl.LiquibaseMigration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class V004__UserRoleTable extends LiquibaseMigration {

    @Override
    protected List<String> getXmlChecksum() {
        return List.of("V004__UserRoleTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V004__UserRoleTable.xml");

        // giving root permission to default user
        InsertQuery insertQuery = new InsertQuery("tbl_user_role");
        insertQuery.addValue("user_role_id", ":user_role_id", Map.of("user_role_id", UUID.randomUUID().toString()));
        insertQuery.addValue("user_id", "(SELECT user_id FROM tbl_user WHERE login = :user)", Map.of("user", V003__UserTable.ADMIN_LOGIN));
        insertQuery.addValue("role_id", "(SELECT role_id FROM tbl_role WHERE name = :role)", Map.of("role", Role.NAME_ROOT));
        named.update(insertQuery.toSQL(), insertQuery.toParam());
    }

}