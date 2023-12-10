package com.senior.kilde.assignment.scommon;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JdbcManager {

    protected static final String FIND_ROLE_ID_BY_NAME = "SELECT role_id FROM tbl_role WHERE name = ?";
    protected static final String FIND_GROUP_ID_BY_NAME = "SELECT group_id FROM tbl_group WHERE name = ?";
    protected static final String FIND_USER_ID_BY_LOGIN = "SELECT user_id FROM tbl_user WHERE login = ?";

    protected final JdbcTemplate jdbc;

    protected JdbcManager(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    protected boolean groupExists(String name) {
        return Boolean.TRUE.equals(this.jdbc.queryForObject("SELECT COUNT(group_id) FROM tbl_group WHERE name = ?", Boolean.class, name));
    }

    protected boolean roleExists(String name) {
        return Boolean.TRUE.equals(this.jdbc.queryForObject("SELECT COUNT(role_id) FROM tbl_role WHERE name = ?", Boolean.class, name));
    }

    protected boolean userExists(String login) {
        return Boolean.TRUE.equals(this.jdbc.queryForObject("SELECT COUNT(user_id) FROM tbl_user WHERE login = ?", Boolean.class, login));
    }

    protected Map<String, String> lookupRoles() {
        ResultSetExtractor<Map<String, String>> roleMapper = (rs) -> {
            Map<String, String> temp = new HashMap<>();
            while (rs.next()) {
                temp.put(rs.getString("name"), rs.getString("role_id"));
            }
            return temp;
        };
        Map<String, String> roles = this.jdbc.query("SELECT role_id, name FROM tbl_role", roleMapper);
        if (roles == null) {
            roles = Collections.emptyMap();
        }
        return roles;
    }

}

