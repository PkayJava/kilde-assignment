package com.senior.kilde.assignment.web.pages.user;

import com.senior.kilde.assignment.web.BaseTest;
import com.senior.kilde.assignment.web.pages.LoginPage;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserBrowsePageTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBrowsePageTest.class);

    @Test
    public void landingLoginPage() {
        this.tester.startPage(UserBrowsePage.class);
        this.tester.assertRenderedPage(LoginPage.class);
    }

    @Test
    public void landingPageTest() {
        this.tester.startPage(UserBrowsePage.class);

        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("username_field", "admin");
        formTester.setValue("password_field", "admin");

        formTester.submit("loginButton");

        this.tester.startPage(UserBrowsePage.class);
        this.tester.assertRenderedPage(UserBrowsePage.class);
    }

}
