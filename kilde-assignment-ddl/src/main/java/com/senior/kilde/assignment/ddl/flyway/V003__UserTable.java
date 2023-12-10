package com.senior.kilde.assignment.ddl.flyway;

import com.senior.cyber.frmk.jdbc.query.InsertQuery;
import com.senior.kilde.assignment.ddl.LiquibaseMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class V003__UserTable extends LiquibaseMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger(V003__UserTable.class);

    public static final String ADMIN_LOGIN = "admin";

    @Override
    protected List<String> getXmlChecksum() {
        return List.of("V003__UserTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V003__UserTable.xml");

        PasswordEncoder password = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // insert default admin user
        InsertQuery insertQuery = new InsertQuery("tbl_user");
        insertQuery.addValue("user_id", ":user_id", Map.of("user_id", UUID.randomUUID().toString()));
        insertQuery.addValue("login", ":login", Map.of("login", ADMIN_LOGIN));
        insertQuery.addValue("password", ":password", Map.of("password", password.encode(ADMIN_LOGIN)));
        insertQuery.addValue("enabled", ":enabled", Map.of("enabled", true));
        insertQuery.addValue("account_non_expired", ":account_non_expired", Map.of("account_non_expired", true));
        insertQuery.addValue("account_non_locked", ":account_non_locked", Map.of("account_non_locked", true));
        insertQuery.addValue("credentials_non_expired", ":credentials_non_expired", Map.of("credentials_non_expired", true));
        named.update(insertQuery.toSQL(), insertQuery.toParam());
    }

}