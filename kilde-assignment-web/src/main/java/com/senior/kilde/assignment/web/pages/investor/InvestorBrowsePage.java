package com.senior.kilde.assignment.web.pages.investor;

import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.cyber.frmk.common.base.WicketFactory;
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
import com.senior.kilde.assignment.dao.entity.Account_;
import com.senior.kilde.assignment.dao.entity.Investor_;
import com.senior.kilde.assignment.dao.entity.Role;
import com.senior.kilde.assignment.scommon.dto.InvestorCreateRequest;
import com.senior.kilde.assignment.scommon.service.InvestorService;
import com.senior.kilde.assignment.web.data.MySqlDataProvider;
import com.senior.kilde.assignment.web.pages.MasterPage;
import com.senior.kilde.assignment.web.validator.InvestorNameValidator;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Bookmark("/investor/browse")
@AuthorizeInstantiation({Role.NAME_ROOT})
public class InvestorBrowsePage extends MasterPage {

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn name_column;
    protected UIContainer name_container;
    protected TextField<String> name_field;
    protected String name_value;

    protected Button createButton;

    protected FilterForm investor_browse_form;
    protected MySqlDataProvider investor_browse_provider;
    protected List<IColumn<Tuple, ? extends Serializable>> investor_browse_column;
    protected DataTable<Tuple, Serializable> investor_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();

        this.investor_browse_provider = new MySqlDataProvider(Sql.table(Investor_.class));
        this.investor_browse_provider.applyJoin("account", "INNER JOIN " + Sql.table(Account_.class) + " ON " + Sql.column(Account_.id) + " = " + Sql.column(Investor_.account));
        this.investor_browse_provider.setSort("name", SortOrder.DESCENDING);
        this.investor_browse_provider.applyCount(Sql.column(Investor_.id));

        this.investor_browse_provider.applySelect(String.class, "id", Sql.column(Investor_.id));

        this.investor_browse_column = new ArrayList<>();
        {
            String label = "Name";
            String key = "name";
            String sql = Sql.column(Investor_.name);
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
            this.investor_browse_column.add(this.investor_browse_provider.filteredColumn(String.class, Model.of(label), key, sql, serializer, filter, deserializer));
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
            this.investor_browse_column.add(this.investor_browse_provider.filteredColumn(String.class, Model.of(label), key, sql, serializer, filter, deserializer));
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
            this.investor_browse_column.add(this.investor_browse_provider.filteredColumn(Double.class, Model.of(label), key, sql, serializer, filter, deserializer));
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
        this.name_field.add(new InvestorNameValidator());
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

        this.investor_browse_form = new FilterForm("investor_browse_form", this.investor_browse_provider);
        body.add(this.investor_browse_form);

        this.investor_browse_table = new DefaultDataTable<>("investor_browse_table", this.investor_browse_column, this.investor_browse_provider, 20);
        this.investor_browse_table.addTopToolbar(new FilterToolbar<>(this.investor_browse_table, this.investor_browse_form));
        this.investor_browse_form.add(this.investor_browse_table);
    }

    protected void createButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        InvestorService service = context.getBean(InvestorService.class);

        InvestorCreateRequest request = new InvestorCreateRequest();
        request.setInitialBalanceAmount(BigDecimal.valueOf(0D));
        request.setName(this.name_value);
        service.investorCreate(request);

        setResponsePage(InvestorBrowsePage.class);
    }

}
