package com.senior.kilde.assignment.web.pages.user;

import com.senior.kilde.assignment.web.factory.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.ContentPanel;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.Tab;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import com.senior.kilde.assignment.dao.entity.User;
import com.senior.kilde.assignment.dao.repository.UserRepository;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

public class UserModifyPageInfoTab extends ContentPanel {

    protected String uuid;

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn login_column;
    protected UIContainer login_container;
    protected TextField<String> login_field;
    protected String login_value;

    protected Button saveButton;
    protected BookmarkablePageLink<Void> cancelButton;

    public UserModifyPageInfoTab(String id, String name, TabbedPanel<Tab> containerPanel, Map<String, Object> data) {
        super(id, name, containerPanel, data);
    }

    @Override
    protected void onInitData() {
        this.uuid = getPage().getPageParameters().get("id").toString();
        loadData();
    }

    protected void loadData() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository userRepository = context.getBean(UserRepository.class);

        Optional<User> userOptional = userRepository.findById(this.uuid);

        User user = userOptional.orElseThrow();

        this.login_value = user.getLogin();
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.login_column = this.row1.newUIColumn("login_column", Size.Six_6);
        this.login_container = this.login_column.newUIContainer("login_container");
        this.login_field = new TextField<>("login_field", new PropertyModel<>(this, "login_value"));
        this.login_field.setLabel(Model.of("Login"));
        this.login_field.setEnabled(false);
        this.login_field.add(new ContainerFeedbackBehavior());
        this.login_container.add(this.login_field);
        this.login_container.newFeedback("login_feedback", this.login_field);

        this.row1.lastUIColumn("last_column");

        this.saveButton = new Button("saveButton") {
            @Override
            public void onSubmit() {
                saveButtonClick();
            }
        };
        this.form.add(this.saveButton);

        this.cancelButton = new BookmarkablePageLink<>("cancelButton", UserBrowsePage.class);
        this.form.add(this.cancelButton);
    }

    protected void saveButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository userRepository = context.getBean(UserRepository.class);

        Optional<User> userOptional = userRepository.findById(this.uuid);

        User user = userOptional.orElseThrow();

        userRepository.save(user);

        setResponsePage(UserBrowsePage.class);
    }

}
