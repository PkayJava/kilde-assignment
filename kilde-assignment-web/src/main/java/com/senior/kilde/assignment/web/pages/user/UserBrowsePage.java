package com.senior.kilde.assignment.web.pages.user;

import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.kilde.assignment.web.factory.WicketFactory;
import com.senior.cyber.frmk.common.jakarta.persistence.Sql;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.IColumn;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.*;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.util.AbstractJdbcDataProvider;
import com.senior.cyber.frmk.common.wicket.functional.DeserializerFunction;
import com.senior.cyber.frmk.common.wicket.functional.FilterFunction;
import com.senior.cyber.frmk.common.wicket.functional.SerializerFunction;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import com.senior.kilde.assignment.dao.entity.Role;
import com.senior.kilde.assignment.dao.entity.User;
import com.senior.kilde.assignment.dao.entity.User_;
import com.senior.kilde.assignment.dao.repository.UserRepository;
import com.senior.kilde.assignment.web.data.MySqlDataProvider;
import com.senior.kilde.assignment.web.pages.MasterPage;
import com.senior.kilde.assignment.web.validator.UserLoginValidator;
import jakarta.persistence.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Bookmark("/user/browse")
@AuthorizeInstantiation({Role.NAME_ROOT})
public class UserBrowsePage extends MasterPage {

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn login_column;
    protected UIContainer login_container;
    protected TextField<String> login_field;
    protected String login_value;

    protected UIColumn password_column;
    protected UIContainer password_container;
    protected TextField<String> password_field;
    protected String password_value;

    protected Button createButton;

    protected FilterForm user_browse_form;
    protected MySqlDataProvider user_browse_provider;
    protected List<IColumn<Tuple, ? extends Serializable>> user_browse_column;
    protected DataTable<Tuple, Serializable> user_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();
        this.user_browse_provider = new MySqlDataProvider(Sql.table(User_.class));
        this.user_browse_provider.applyCount(Sql.column(User_.id));
        this.user_browse_provider.applySelect(String.class, "uuid", Sql.column(User_.id));

        this.user_browse_provider.setSort("login", SortOrder.ASCENDING);

        this.user_browse_column = new ArrayList<>();
         {
            String label = "Login";
            String key = "login";
            String sql = Sql.column(User_.login);
            SerializerFunction<String> serializer = (value) -> value;
            DeserializerFunction<String> deserializer = (value) -> value;
            FilterFunction<String> filter = (count, alias, params, filterText) -> {
                String v = StringUtils.trimToEmpty(deserializer.apply(filterText));
                if (!v.isEmpty()) {
                    params.put(key, v + "%");
                    return List.of(AbstractJdbcDataProvider.WHERE + sql + " LIKE :" + key);
                } else {
                    return null;
                }
            };
            this.user_browse_column.add(this.user_browse_provider.filteredColumn(String.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        {
            String label = "Enabled";
            String key = "enabled";
            String sql = Sql.column(User_.enabled);
            SerializerFunction<Boolean> serializer = (value) -> {
                if (value == null || !value) {
                    return "No";
                } else {
                    return "Yes";
                }
            };
            this.user_browse_column.add(this.user_browse_provider.column(Boolean.class, Model.of(label), key, sql, serializer));
        }
        this.user_browse_column.add(new ActionFilteredColumn<>(Model.of("Action"), this::user_browse_action_link, this::user_browse_action_click));
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.login_column = this.row1.newUIColumn("login_column", Size.Three_3);
        this.login_container = this.login_column.newUIContainer("login_container");
        this.login_field = new TextField<>("login_field", new PropertyModel<>(this, "login_value"));
        this.login_field.setLabel(Model.of("Login"));
        this.login_field.setRequired(true);
        this.login_field.add(new UserLoginValidator());
        this.login_field.add(new ContainerFeedbackBehavior());
        this.login_container.add(this.login_field);
        this.login_container.newFeedback("login_feedback", this.login_field);

        this.password_column = this.row1.newUIColumn("password_column", Size.Three_3);
        this.password_container = this.password_column.newUIContainer("password_container");
        this.password_field = new TextField<>("password_field", new PropertyModel<>(this, "password_value"));
        this.password_field.setLabel(Model.of("Password"));
        this.password_field.setRequired(true);
        this.password_field.add(new ContainerFeedbackBehavior());
        this.password_container.add(this.password_field);
        this.password_container.newFeedback("password_feedback", this.password_field);

        this.row1.lastUIColumn("last_column");

        this.createButton = new Button("createButton") {
            @Override
            public void onSubmit() {
                createButtonClick();
            }
        };
        this.form.add(this.createButton);

        this.user_browse_form = new FilterForm("user_browse_form", this.user_browse_provider);
        body.add(this.user_browse_form);

        this.user_browse_table = new DefaultDataTable<>("user_browse_table", this.user_browse_column, this.user_browse_provider, 20);
        this.user_browse_table.addTopToolbar(new FilterToolbar<>(this.user_browse_table, this.user_browse_form));
        this.user_browse_form.add(this.user_browse_table);
    }

    protected void createButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository repository = context.getBean(UserRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        User user = new User();
        user.setPassword(passwordEncoder.encode(this.password_value));
        user.setLogin(this.login_value);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        repository.save(user);

        setResponsePage(UserBrowsePage.class);
    }

    protected List<ActionItem> user_browse_action_link(String link, Tuple model) {
        List<ActionItem> actions = new ArrayList<>(0);
        boolean enabled = model.get("enabled", boolean.class);
        actions.add(new ActionItem("Edit", Model.of("Edit"), ItemCss.SUCCESS));
        if (enabled) {
            actions.add(new ActionItem("Disable", Model.of("Disable"), ItemCss.DANGER));
        } else {
            actions.add(new ActionItem("Enable", Model.of("Enable"), ItemCss.DANGER));
        }
        return actions;
    }

    protected void user_browse_action_click(String link, Tuple model, AjaxRequestTarget target) {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository userRepository = context.getBean(UserRepository.class);

        String uuid = model.get("uuid", String.class);

        if ("Edit".equals(link)) {
            PageParameters parameters = new PageParameters();
            parameters.add("id", uuid);
            setResponsePage(UserModifyPage.class, parameters);
        } else if ("Disable".equals(link)) {
            Optional<User> userOptional = userRepository.findById(uuid);
            User user = userOptional.orElseThrow();
            user.setEnabled(false);
            userRepository.save(user);
            target.add(this.user_browse_table);
        } else if ("Enable".equals(link)) {
            Optional<User> userOptional = userRepository.findById(uuid);
            User user = userOptional.orElseThrow();
            user.setEnabled(true);
            userRepository.save(user);
            target.add(this.user_browse_table);
        }
    }

}
