package com.senior.kilde.assignment.web;

import com.senior.cyber.frmk.common.base.WebUiProperties;
import com.senior.kilde.assignment.web.factory.WicketApplication;
import com.senior.kilde.assignment.web.factory.WicketFactory;
import jakarta.servlet.ServletContext;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class BaseTest {

    protected WicketTester tester;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private WebUiProperties webUiProperties;

    @BeforeEach
    public void setup() {
        WicketFactory.setApplicationContext(context);
        WicketApplication application = new WicketApplication(this.webUiProperties);
        this.tester = new WicketTester(application);
    }

}
