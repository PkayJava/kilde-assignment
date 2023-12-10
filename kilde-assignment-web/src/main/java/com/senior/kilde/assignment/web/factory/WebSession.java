package com.senior.kilde.assignment.web.factory;

import com.senior.kilde.assignment.dao.entity.User;
import com.senior.kilde.assignment.web.utility.RoleUtility;
import com.senior.kilde.assignment.web.utility.UserUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class WebSession extends AuthenticatedWebSession {

    @lombok.Getter
    protected String userId;

    protected Roles roles;

    protected String sessionId;

    public WebSession(Request request) {
        super(request);
        HttpServletRequest req = (HttpServletRequest) request.getContainerRequest();
        this.sessionId = req.getSession(true).getId();
    }

    @Override
    public Roles getRoles() {
        return this.roles;
    }

    @Override
    protected boolean authenticate(String username, String password) {
        ApplicationContext context = WicketFactory.getApplicationContext();

        User user = UserUtility.authenticate(username, password);

        if (user == null) {
            return false;
        }

        NamedParameterJdbcTemplate named = context.getBean(NamedParameterJdbcTemplate.class);

        List<String> roles = RoleUtility.lookupRole(named, user.getId());
        this.roles = new Roles();
        this.roles.addAll(roles);
        this.userId = user.getId();

        return true;
    }


}
