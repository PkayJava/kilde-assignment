package com.senior.kilde.assignment.scommon;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;
import java.util.*;

public class JdbcUserManager extends JdbcManager implements UserDetailsManager {

    private AuthenticationManager authenticationManager;

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public JdbcUserManager(DataSource dataSource) {
        super(dataSource);
    }

    public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
        this.securityContextHolderStrategy = securityContextHolderStrategy;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void createUser(UserDetails user) {
        updateUser(user);
    }

    @Override
    public void updateUser(UserDetails user) {
        boolean userExisted = userExists(user.getUsername());
        if (!userExisted) {
            String id = UUID.randomUUID().toString();
            this.jdbc.update("INSERT INTO tbl_user(user_id, login, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES(?, ?, ?, ?, ?, ?, ?)", id, user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired());
        } else {
            this.jdbc.update("UPDATE tbl_user SET password = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? WHERE login = ?", user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.getUsername());
        }

        String userId = this.jdbc.queryForObject(FIND_USER_ID_BY_LOGIN, String.class, user.getUsername());

        List<String> proposed = new ArrayList<>();
        for (GrantedAuthority authority : user.getAuthorities()) {
            proposed.add(authority.getAuthority());
        }

        Map<String, String> roles = lookupRoles();

        // insert operation
        {
            List<String> rolesInGroup = lookupRolesInGroup(user.getUsername());
            List<String> rolesInUser = lookupRolesInUser(user.getUsername());
            List<String> rolesInDeny = lookupRolesInDeny(user.getUsername());
            for (String role : proposed) {
                if (rolesInDeny.contains(role)) {
                    this.jdbc.update("DELETE FROM tbl_deny_role WHERE role_id = ? AND user_id = ?", roles.get(role), userId);
                }
                if (!rolesInGroup.contains(role) && !rolesInUser.contains(role)) {
                    if (!roles.containsKey(role)) {
                        String id = UUID.randomUUID().toString();
                        this.jdbc.update("INSERT INTO tbl_role(role_id, name, enabled, description) VALUES(?, ?, ?, ?)", id, role, true, "N/A");
                        roles.put(role, id);
                    }
                    String id = UUID.randomUUID().toString();
                    this.jdbc.update("INSERT INTO tbl_user_role(user_role_id, user_id, role_id) VALUES(?, ?, ?)", id, userId, roles.get(role));
                }
            }
        }

        // remove operation
        {
            List<String> rolesInGroup = lookupRolesInGroup(user.getUsername());
            List<String> rolesInUser = lookupRolesInUser(user.getUsername());
            List<String> rolesInDeny = lookupRolesInDeny(user.getUsername());
            List<String> p = null;
            p = new ArrayList<>();
            for (String role : rolesInGroup) {
                if (!proposed.contains(role)) {
                    boolean existed = Boolean.TRUE.equals(this.jdbc.queryForObject("SELECT COUNT(deny_role_id) FROM tbl_deny_role WHERE user_id = ? AND role_id = ?", Boolean.class, userId, roles.get(role)));
                    if (!existed) {
                        String id = UUID.randomUUID().toString();
                        this.jdbc.update("INSERT INTO tbl_deny_role(deny_role_id, user_id, role_id) VALUES(?, ?, ?)", id, userId, roles.get(role));
                    }
                    p.add(role);
                }
            }
            proposed.removeAll(p);

            p = new ArrayList<>();
            for (String role : rolesInUser) {
                if (!proposed.contains(role)) {
                    this.jdbc.update("DELETE FROM tbl_user_role WHERE user_id = ? and role_id = ?", userId, roles.get(role));
                    if (!rolesInDeny.contains(role)) {
                        this.jdbc.update("DELETE FROM tbl_deny_role WHERE user_id = ? and role_id = ?", userId, roles.get(role));
                    }
                    p.add(role);
                }
            }
            proposed.removeAll(p);

            for (String role : proposed) {
                this.jdbc.update("DELETE FROM tbl_deny_role WHERE user_id = ? and role_id = ?", userId, roles.get(role));
            }
        }
    }

    @Override
    public void deleteUser(String username) {
        if (userExists(username)) {
            this.jdbc.update("DELETE FROM tbl_user_group WHERE user_id = (" + FIND_USER_ID_BY_LOGIN + ")", username);
            this.jdbc.update("DELETE FROM tbl_user_role WHERE user_id = (" + FIND_USER_ID_BY_LOGIN + ")", username);
            this.jdbc.update("DELETE FROM tbl_deny_role WHERE user_id = (" + FIND_USER_ID_BY_LOGIN + ")", username);
            this.jdbc.update("DELETE FROM tbl_user WHERE login = ?", username);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("Can't change password as no Authentication object found in context " + "for current user.");
        }
        String username = currentUser.getName();
        // If an authentication manager has been set, re-authenticate the user with the
        // supplied password.
        if (this.authenticationManager != null) {
            this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
        }
        this.jdbc.update("UPDATE tbl_user SET password = ? WHERE login = ?", newPassword, username);
        Authentication authentication = createNewAuthentication(currentUser, newPassword);
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
    }

    @Override
    public boolean userExists(String username) {
        return super.userExists(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean userExisted = userExists(username);
        if (!userExisted) {
            throw new UsernameNotFoundException(username + " is not found");
        }

        RowMapper<UserDetails> userMapper = (rs1, row1) -> {
            String userId = rs1.getString("user_id");
            String login = rs1.getString("login");
            String password = rs1.getString("password");
            boolean enabled = rs1.getBoolean("enabled");
            boolean accountNonExpired = rs1.getBoolean("account_non_expired");
            boolean credentialsNonExpired = rs1.getBoolean("credentials_non_expired");
            boolean accountNonLocked = rs1.getBoolean("account_non_locked");

            List<String> lines = new ArrayList<>();
            lines.add("SELECT distinct u.name FROM");
            lines.add("(");
            lines.add("SELECT tbl_role.name FROM tbl_user");
            lines.add("INNER JOIN tbl_user_group ON tbl_user_group.user_id = tbl_user.user_id");
            lines.add("INNER JOIN tbl_group ON tbl_group.group_id = tbl_user_group.group_id");
            lines.add("INNER JOIN tbl_group_role ON tbl_group_role.group_id = tbl_group.group_id");
            lines.add("INNER JOIN tbl_role ON tbl_role.role_id = tbl_group_role.role_id");
            lines.add("WHERE tbl_role.enabled = ?");
            lines.add("AND tbl_group.enabled = ?");
            lines.add("AND tbl_user.user_id = ?");
            lines.add("UNION");
            lines.add("SELECT tbl_role.name FROM tbl_user");
            lines.add("INNER JOIN tbl_user_role ON tbl_user_role.user_id = tbl_user.user_id");
            lines.add("INNER JOIN tbl_role ON tbl_role.role_id = tbl_user_role.role_id");
            lines.add("WHERE tbl_role.enabled = ?");
            lines.add("AND tbl_user.user_id = ?");
            lines.add(") u");
            lines.add("WHERE u.name NOT IN (");
            lines.add("SELECT tbl_role.name FROM tbl_user");
            lines.add("INNER JOIN tbl_deny_role ON tbl_deny_role.user_id = tbl_user.user_id");
            lines.add("INNER JOIN tbl_role ON tbl_role.role_id = tbl_deny_role.role_id");
            lines.add("WHERE tbl_role.enabled = ?");
            lines.add("AND tbl_user.user_id = ?");
            lines.add(")");

            Collection<? extends GrantedAuthority> authorities = this.jdbc.query(StringUtils.join(lines, " "), (rs2, row2) -> {
                String name = rs2.getString("name");
                return new SimpleGrantedAuthority(name);
            }, true, true, userId, true, userId, true, userId);

            return User.withUsername(login)
                    .password(password)
                    .authorities(authorities)
                    .disabled(!enabled)
                    .accountExpired(!accountNonExpired)
                    .accountLocked(!accountNonLocked)
                    .credentialsExpired(!credentialsNonExpired).build();
        };
        return this.jdbc.queryForObject("SELECT user_id, login, password, enabled, account_non_expired, account_non_locked, credentials_non_expired FROM tbl_user WHERE login = ?", userMapper, username);
    }

    protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        UserDetails user = loadUserByUsername(currentAuth.getName());
        UsernamePasswordAuthenticationToken newAuthentication = UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }

    protected List<String> lookupRolesInGroup(String login) {
        List<String> lines = new ArrayList<>();
        lines.add("SELECT tbl_role.name FROM tbl_user");
        lines.add("INNER JOIN tbl_user_group ON tbl_user_group.user_id = tbl_user.user_id");
        lines.add("INNER JOIN tbl_group ON tbl_group.group_id = tbl_user_group.group_id");
        lines.add("INNER JOIN tbl_group_role ON tbl_group_role.group_id = tbl_group.group_id");
        lines.add("INNER JOIN tbl_role ON tbl_role.role_id = tbl_group_role.role_id");
        lines.add("WHERE tbl_user.login = ?");
        return this.jdbc.queryForList(StringUtils.join(lines, " "), String.class, login);
    }

    protected List<String> lookupRolesInUser(String login) {
        List<String> lines = new ArrayList<>();
        lines.add("SELECT tbl_role.name FROM tbl_user");
        lines.add("INNER JOIN tbl_user_role ON tbl_user_role.user_id = tbl_user.user_id");
        lines.add("INNER JOIN tbl_role ON tbl_role.role_id = tbl_user_role.role_id");
        lines.add("WHERE tbl_user.login = ?");
        return this.jdbc.queryForList(StringUtils.join(lines, " "), String.class, login);
    }

    protected List<String> lookupRolesInDeny(String login) {
        List<String> lines = new ArrayList<>();
        lines.add("SELECT tbl_role.name FROM tbl_user");
        lines.add("INNER JOIN tbl_deny_role ON tbl_deny_role.user_id = tbl_user.user_id");
        lines.add("INNER JOIN tbl_role ON tbl_role.role_id = tbl_deny_role.role_id");
        lines.add("WHERE tbl_user.login = ?");
        return this.jdbc.queryForList(StringUtils.join(lines, " "), String.class, login);
    }

}

