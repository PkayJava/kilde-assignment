package com.senior.kilde.assignment.web.pages.user;

import com.senior.cyber.frmk.common.base.WebUiProperties;
import com.senior.kilde.assignment.web.factory.WicketApplication;
import com.senior.kilde.assignment.web.factory.WicketFactory;
import com.senior.kilde.assignment.web.pages.LoginPage;
import jakarta.servlet.ServletContext;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class UserBrowsePageTest {

    private WicketTester tester;

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

    @Test
    public void landingPageTest() {
        this.tester.startPage(UserBrowsePage.class);
        this.tester.assertRenderedPage(LoginPage.class);
    }

}
