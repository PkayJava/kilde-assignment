package com.senior.kilde.assignment.web.pages.tranche;

import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.cyber.frmk.common.jakarta.persistence.Sql;
import com.senior.cyber.frmk.common.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.IColumn;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.*;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.util.AbstractJdbcDataProvider;
import com.senior.cyber.frmk.common.wicket.functional.DeserializerFunction;
import com.senior.cyber.frmk.common.wicket.functional.FilterFunction;
import com.senior.cyber.frmk.common.wicket.functional.SerializerFunction;
import com.senior.kilde.assignment.dao.entity.Role;
import com.senior.kilde.assignment.dao.entity.Tranche_;
import com.senior.kilde.assignment.web.data.MySqlDataProvider;
import com.senior.kilde.assignment.web.pages.MasterPage;
import jakarta.persistence.Tuple;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Bookmark("/tranche/browse")
@AuthorizeInstantiation({Role.NAME_ROOT})
public class TrancheBrowsePage extends MasterPage {

    private static final DecimalFormat FORMAT = new DecimalFormat("#,###,###,##0.00");

    protected BookmarkablePageLink<Void> createButton;

    protected FilterForm tranche_browse_form;
    protected MySqlDataProvider tranche_browse_provider;
    protected List<IColumn<Tuple, ? extends Serializable>> tranche_browse_column;
    protected DataTable<Tuple, Serializable> tranche_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();

        this.tranche_browse_provider = new MySqlDataProvider(Sql.table(Tranche_.class));
        this.tranche_browse_provider.applyCount(Sql.column(Tranche_.id));
        this.tranche_browse_provider.applySelect(String.class, "uuid", Sql.column(Tranche_.id));
        this.tranche_browse_provider.setSort("name", SortOrder.ASCENDING);

        this.tranche_browse_column = new ArrayList<>();
        {
            String label = "Name";
            String key = "name";
            String sql = Sql.column(Tranche_.name);
            SerializerFunction<String> serializer = (value) -> {
                if (value == null) {
                    return "";
                } else {
                    return value;
                }
            };
            DeserializerFunction<String> deserializer = (value) -> {
                if (value == null || value.isEmpty()) {
                    return null;
                } else {
                    return value;
                }
            };
            FilterFunction<String> filter = (count, alias, params, filterText) -> {
                String v = deserializer.apply(filterText);
                if (v != null) {
                    params.put(key, v);
                    return List.of(AbstractJdbcDataProvider.WHERE + sql + " = :" + key);
                } else {
                    return null;
                }
            };
            this.tranche_browse_column.add(this.tranche_browse_provider.filteredColumn(String.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        {
            String label = "Duration";
            String key = "Duration";
            String sql = Sql.column(Tranche_.duration);
            SerializerFunction<Integer> serializer = (value) -> {
                if (value == null) {
                    return "N/A";
                } else {
                    if (value > 1) {
                        return String.valueOf(value) + " Months";
                    } else {
                        return String.valueOf(value) + " Month";
                    }
                }
            };
            DeserializerFunction<Integer> deserializer = (value) -> {
                if (value == null || value.isEmpty()) {
                    return null;
                } else {
                    return Integer.valueOf(value);
                }
            };
            FilterFunction<Integer> filter = (count, alias, params, filterText) -> {
                Integer v = deserializer.apply(filterText);
                if (v != null) {
                    params.put(key, v);
                    return List.of(AbstractJdbcDataProvider.WHERE + sql + " = :" + key);
                } else {
                    return null;
                }
            };
            this.tranche_browse_column.add(this.tranche_browse_provider.filteredColumn(Integer.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        {
            String label = "Interest";
            String key = "Interest";
            String sql = Sql.column(Tranche_.annualInterest);
            SerializerFunction<Float> serializer = (value) -> {
                if (value == null) {
                    return "N/A";
                } else {
                    return String.valueOf(value) + " % per year";
                }
            };
            DeserializerFunction<Float> deserializer = (value) -> {
                if (value == null || value.isEmpty()) {
                    return null;
                } else {
                    return Float.valueOf(value);
                }
            };
            FilterFunction<Float> filter = (count, alias, params, filterText) -> {
                Float v = deserializer.apply(filterText);
                if (v != null) {
                    params.put(key, v);
                    return List.of(AbstractJdbcDataProvider.WHERE + sql + " = :" + key);
                } else {
                    return null;
                }
            };
            this.tranche_browse_column.add(this.tranche_browse_provider.filteredColumn(Float.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        {
            String label = "Max Invest Amount / Investor";
            String key = "maximumInvestmentAmountPerInvestor";
            String sql = Sql.column(Tranche_.maximumInvestmentAmountPerInvestor);
            SerializerFunction<Double> serializer = (value) -> {
                if (value == null) {
                    return "N/A";
                } else {
                    return FORMAT.format(value) + " SGD";
                }
            };
            DeserializerFunction<Double> deserializer = (value) -> {
                if (value == null || value.isEmpty()) {
                    return null;
                } else {
                    return Double.valueOf(value);
                }
            };
            FilterFunction<Double> filter = (count, alias, params, filterText) -> {
                Double v = deserializer.apply(filterText);
                if (v != null) {
                    params.put(key, v);
                    return List.of(AbstractJdbcDataProvider.WHERE + sql + " = :" + key);
                } else {
                    return null;
                }
            };
            this.tranche_browse_column.add(this.tranche_browse_provider.filteredColumn(Double.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        {
            String label = "Min Invest Amount";
            String key = "minimumInvestmentAmount";
            String sql = Sql.column(Tranche_.minimumInvestmentAmount);
            SerializerFunction<Double> serializer = (value) -> {
                if (value == null) {
                    return "N/A";
                } else {
                    return FORMAT.format(value) + " SGD";
                }
            };
            DeserializerFunction<Double> deserializer = (value) -> {
                if (value == null || value.isEmpty()) {
                    return null;
                } else {
                    return Double.valueOf(value);
                }
            };
            FilterFunction<Double> filter = (count, alias, params, filterText) -> {
                Double v = deserializer.apply(filterText);
                if (v != null) {
                    params.put(key, v);
                    return List.of(AbstractJdbcDataProvider.WHERE + sql + " = :" + key);
                } else {
                    return null;
                }
            };
            this.tranche_browse_column.add(this.tranche_browse_provider.filteredColumn(Double.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        {
            String label = "Max Invest Amount";
            String key = "maximumInvestmentAmount";
            String sql = Sql.column(Tranche_.maximumInvestmentAmount);
            SerializerFunction<Double> serializer = (value) -> {
                if (value == null) {
                    return "N/A";
                } else {
                    return FORMAT.format(value) + " SGD";
                }
            };
            DeserializerFunction<Double> deserializer = (value) -> {
                if (value == null || value.isEmpty()) {
                    return null;
                } else {
                    return Double.valueOf(value);
                }
            };
            FilterFunction<Double> filter = (count, alias, params, filterText) -> {
                Double v = deserializer.apply(filterText);
                if (v != null) {
                    params.put(key, v);
                    return List.of(AbstractJdbcDataProvider.WHERE + sql + " = :" + key);
                } else {
                    return null;
                }
            };
            this.tranche_browse_column.add(this.tranche_browse_provider.filteredColumn(Double.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        {
            String label = "Amount Avail For Invest";
            String key = "amountAvailableForInvestment";
            String sql = Sql.column(Tranche_.amountAvailableForInvestment);
            SerializerFunction<Double> serializer = (value) -> {
                if (value == null) {
                    return "N/A";
                } else {
                    return FORMAT.format(value) + " SGD";
                }
            };
            DeserializerFunction<Double> deserializer = (value) -> {
                if (value == null || value.isEmpty()) {
                    return null;
                } else {
                    return Double.valueOf(value);
                }
            };
            FilterFunction<Double> filter = (count, alias, params, filterText) -> {
                Double v = deserializer.apply(filterText);
                if (v != null) {
                    params.put(key, v);
                    return List.of(AbstractJdbcDataProvider.WHERE + sql + " = :" + key);
                } else {
                    return null;
                }
            };
            this.tranche_browse_column.add(this.tranche_browse_provider.filteredColumn(Double.class, Model.of(label), key, sql, serializer, filter, deserializer));
        }
        this.tranche_browse_column.add(new ActionFilteredColumn<>(Model.of("Action"), this::tranche_browse_action_link, this::tranche_browse_action_click));
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.createButton = new BookmarkablePageLink<>("createButton", TrancheCreatePage.class);
        body.add(this.createButton);

        this.tranche_browse_form = new FilterForm("tranche_browse_form", this.tranche_browse_provider);
        body.add(this.tranche_browse_form);

        this.tranche_browse_table = new AjaxFallbackDefaultDataTable<>("tranche_browse_table", this.tranche_browse_column, this.tranche_browse_provider, 50);
        this.tranche_browse_table.addTopToolbar(new FilterToolbar<>(this.tranche_browse_table, this.tranche_browse_form));
        this.tranche_browse_form.add(this.tranche_browse_table);
    }

    protected List<ActionItem> tranche_browse_action_link(String link, Tuple model) {
        List<ActionItem> actions = new ArrayList<>(0);
//        actions.add(new ActionItem("Edit", Model.of("Edit"), ItemCss.SUCCESS));
        return actions;
    }

    protected void tranche_browse_action_click(String link, Tuple model, AjaxRequestTarget target) {
        String uuid = model.get("uuid", String.class);
        if ("Edit".equals(link)) {
            PageParameters parameters = new PageParameters();
            parameters.add("uuid", uuid);
            setResponsePage(TrancheModifyPage.class, parameters);
        }
    }

}
