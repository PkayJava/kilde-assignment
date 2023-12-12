package com.senior.kilde.assignment.web.pages.account;

import com.senior.cyber.frmk.common.base.Bookmark;
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
import com.senior.kilde.assignment.dao.entity.Account_;
import com.senior.kilde.assignment.dao.entity.Role;
import com.senior.kilde.assignment.web.data.MySqlDataProvider;
import com.senior.kilde.assignment.web.pages.MasterPage;
import jakarta.persistence.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Bookmark("/account/browse")
@AuthorizeInstantiation({Role.NAME_ROOT})
public class AccountBrowsePage extends MasterPage {

    protected FilterForm account_browse_form;
    protected MySqlDataProvider account_browse_provider;
    protected List<IColumn<Tuple, ? extends Serializable>> account_browse_column;
    protected DataTable<Tuple, Serializable> account_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();

        this.account_browse_provider = new MySqlDataProvider(Sql.table(Account_.class));
        this.account_browse_provider.setSort("account_no", SortOrder.DESCENDING);
        this.account_browse_provider.applyCount(Sql.column(Account_.id));

        this.account_browse_provider.applySelect(String.class, "id", Sql.column(Account_.id));

        this.account_browse_column = new ArrayList<>();
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
            this.account_browse_column.add(this.account_browse_provider.filteredColumn(String.class, Model.of(label), key, sql, serializer, filter, deserializer));
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
            this.account_browse_column.add(this.account_browse_provider.filteredColumn(Double.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.account_browse_form = new FilterForm("account_browse_form", this.account_browse_provider);
        body.add(this.account_browse_form);

        this.account_browse_table = new DefaultDataTable<>("account_browse_table", this.account_browse_column, this.account_browse_provider, 20);
        this.account_browse_table.addTopToolbar(new FilterToolbar<>(this.account_browse_table, this.account_browse_form));
        this.account_browse_form.add(this.account_browse_table);
    }

}
