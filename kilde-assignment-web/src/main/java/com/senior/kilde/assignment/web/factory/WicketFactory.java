package com.senior.kilde.assignment.web.factory;

import com.senior.cyber.frmk.common.base.AbstractWicketFactory;
import com.senior.cyber.frmk.common.base.WebUiProperties;
import com.senior.cyber.frmk.common.exception.UnauthorizedInstantiationException;
import com.senior.kilde.assignment.web.pages.DeniedPage;
import com.senior.kilde.assignment.web.pages.ErrorPage;
import com.senior.kilde.assignment.web.pages.LoginPage;
import com.senior.kilde.assignment.web.pages.user.UserBrowsePage;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.ApplicationContext;

public class WicketFactory extends AbstractWicketFactory {

    @Override
    protected WebApplication createApplication(ApplicationContext applicationContext) {
        return new WicketApplication(applicationContext.getBean(WebUiProperties.class));
    }

}
