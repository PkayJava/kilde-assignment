package com.senior.kilde.assignment.web.pages.borrower;

import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.kilde.assignment.web.factory.WicketFactory;
import com.senior.cyber.frmk.common.jakarta.persistence.Sql;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.IColumn;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.util.AbstractJdbcDataProvider;
import com.senior.cyber.frmk.common.wicket.functional.DeserializerFunction;
import com.senior.cyber.frmk.common.wicket.functional.FilterFunction;
import com.senior.cyber.frmk.common.wicket.functional.SerializerFunction;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import com.senior.kilde.assignment.dao.entity.*;
import com.senior.kilde.assignment.dao.repository.BorrowerRepository;
import com.senior.kilde.assignment.scommon.dto.BorrowerCreateRequest;
import com.senior.kilde.assignment.scommon.service.BorrowerService;
import com.senior.kilde.assignment.web.data.MySqlDataProvider;
import com.senior.kilde.assignment.web.pages.MasterPage;
import com.senior.kilde.assignment.web.validator.BorrowerNameValidator;
import jakarta.persistence.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Bookmark("/borrower/browse")
@AuthorizeInstantiation({Role.NAME_ROOT})
public class BorrowerBrowsePage extends MasterPage {

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn name_column;
    protected UIContainer name_container;
    protected TextField<String> name_field;
    protected String name_value;

    protected Button createButton;

    protected FilterForm borrower_browse_form;
    protected MySqlDataProvider borrower_browse_provider;
    protected List<IColumn<Tuple, ? extends Serializable>> borrower_browse_column;
    protected DataTable<Tuple, Serializable> borrower_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();

        this.borrower_browse_provider = new MySqlDataProvider(Sql.table(Borrower_.class));
        this.borrower_browse_provider.applyJoin("account", "INNER JOIN " + Sql.table(Account_.class) + " ON " + Sql.column(Account_.id) + " = " + Sql.column(Borrower_.account));
        this.borrower_browse_provider.setSort("name", SortOrder.DESCENDING);
        this.borrower_browse_provider.applyCount(Sql.column(Borrower_.id));

        this.borrower_browse_provider.applySelect(String.class, "id", Sql.column(Borrower_.id));

        this.borrower_browse_column = new ArrayList<>();
        {
            String label = "Name";
            String key = "name";
            String sql = Sql.column(Borrower_.name);
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
            this.borrower_browse_column.add(this.borrower_browse_provider.filteredColumn(String.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        {
            String label = "Account No";
            String key = "account_no";
            String sql = Sql.column(Account_.accountNo);
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
            this.borrower_browse_column.add(this.borrower_browse_provider.filteredColumn(String.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        {
            String label = "Balance";
            String key = "balance";
            String sql = Sql.column(Account_.balance);
            SerializerFunction<Double> serializer = (value) -> String.valueOf(value);
            DeserializerFunction<Double> deserializer = (value) -> Double.valueOf(value);
            FilterFunction<Double> filter = (count, alias, params, filterText) -> {
                Double v = deserializer.apply(filterText);
                if (v != null) {
                    params.put(key, v + "%");
                    return List.of(AbstractJdbcDataProvider.WHERE + sql + " = :" + key);
                } else {
                    return null;
                }
            };
            this.borrower_browse_column.add(this.borrower_browse_provider.filteredColumn(Double.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.name_column = this.row1.newUIColumn("name_column", Size.Six_6);
        this.name_container = this.name_column.newUIContainer("name_container");
        this.name_field = new TextField<>("name_field", new PropertyModel<>(this, "name_value"));
        this.name_field.setLabel(Model.of("Name"));
        this.name_field.setRequired(true);
        this.name_field.add(new BorrowerNameValidator());
        this.name_field.add(new ContainerFeedbackBehavior());
        this.name_container.add(this.name_field);
        this.name_container.newFeedback("name_feedback", this.name_field);

        this.row1.lastUIColumn("last_column");

        this.createButton = new Button("createButton") {
            @Override
            public void onSubmit() {
                createButtonClick();
            }
        };
        this.form.add(this.createButton);

        this.borrower_browse_form = new FilterForm("borrower_browse_form", this.borrower_browse_provider);
        body.add(this.borrower_browse_form);

        this.borrower_browse_table = new DefaultDataTable<>("borrower_browse_table", this.borrower_browse_column, this.borrower_browse_provider, 20);
        this.borrower_browse_table.addTopToolbar(new FilterToolbar<>(this.borrower_browse_table, this.borrower_browse_form));
        this.borrower_browse_form.add(this.borrower_browse_table);
    }

    protected void createButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        BorrowerService service = context.getBean(BorrowerService.class);

        BorrowerCreateRequest request = new BorrowerCreateRequest();
        request.setName(this.name_value);

        service.borrowerCreate(request);

        setResponsePage(BorrowerBrowsePage.class);
    }

}
