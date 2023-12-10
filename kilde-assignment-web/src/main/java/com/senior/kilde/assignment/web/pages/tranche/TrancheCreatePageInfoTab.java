package com.senior.kilde.assignment.web.pages.tranche;

import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.ContentPanel;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.Tab;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.repository.TrancheRepository;
import com.senior.kilde.assignment.web.validator.TrancheNameValidator;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class TrancheCreatePageInfoTab extends ContentPanel {

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn name_column;
    protected UIContainer name_container;
    protected TextField<String> name_field;
    protected String name_value;

    protected UIColumn annual_interest_column;
    protected UIContainer annual_interest_container;
    protected TextField<Float> annual_interest_field;
    protected Float annual_interest_value;

    protected UIRow row2;

    protected UIColumn amount_available_for_investment_column;
    protected UIContainer amount_available_for_investment_container;
    protected TextField<Double> amount_available_for_investment_field;
    protected Double amount_available_for_investment_value;

    protected UIColumn duration_column;
    protected UIContainer duration_container;
    protected TextField<Integer> duration_field;
    protected Integer duration_value;

    protected UIRow row3;

    protected UIColumn minimum_investment_amount_column;
    protected UIContainer minimum_investment_amount_container;
    protected TextField<Double> minimum_investment_amount_field;
    protected Double minimum_investment_amount_value;

    protected UIColumn maximum_investment_amount_column;
    protected UIContainer maximum_investment_amount_container;
    protected TextField<Double> maximum_investment_amount_field;
    protected Double maximum_investment_amount_value;

    protected UIColumn maximum_investment_amount_per_investor_column;
    protected UIContainer maximum_investment_amount_per_investor_container;
    protected TextField<Double> maximum_investment_amount_per_investor_field;
    protected Double maximum_investment_amount_per_investor_value;

    protected Button saveButton;
    protected BookmarkablePageLink<Void> cancelButton;

    public TrancheCreatePageInfoTab(String id, String name, TabbedPanel<Tab> containerPanel, Map<String, Object> data) {
        super(id, name, containerPanel, data);
    }

    @Override
    protected void onInitData() {
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.name_column = this.row1.newUIColumn("name_column", Size.Four_4);
        this.name_container = this.name_column.newUIContainer("name_container");
        this.name_field = new TextField<>("name_field", new PropertyModel<>(this, "name_value"));
        this.name_field.setLabel(Model.of("Name"));
        this.name_field.add(new ContainerFeedbackBehavior());
        this.name_field.add(new TrancheNameValidator());
        this.name_field.setRequired(true);
        this.name_container.add(this.name_field);
        this.name_container.newFeedback("name_feedback", this.name_field);

        this.annual_interest_column = this.row1.newUIColumn("annual_interest_column", Size.Four_4);
        this.annual_interest_container = this.annual_interest_column.newUIContainer("annual_interest_container");
        this.annual_interest_field = new TextField<>("annual_interest_field", new PropertyModel<>(this, "annual_interest_value"));
        this.annual_interest_field.setLabel(Model.of("Annual Interest"));
        this.annual_interest_field.add(new ContainerFeedbackBehavior());
        this.annual_interest_field.add(RangeValidator.range(0f, 100f));
        this.annual_interest_field.setRequired(true);
        this.annual_interest_container.add(this.annual_interest_field);
        this.annual_interest_container.newFeedback("annual_interest_feedback", this.annual_interest_field);

        this.row1.lastUIColumn("last_column");

        this.row2 = UIRow.newUIRow("row2", this.form);

        this.amount_available_for_investment_column = this.row2.newUIColumn("amount_available_for_investment_column", Size.Four_4);
        this.amount_available_for_investment_container = this.amount_available_for_investment_column.newUIContainer("amount_available_for_investment_container");
        this.amount_available_for_investment_field = new TextField<>("amount_available_for_investment_field", new PropertyModel<>(this, "amount_available_for_investment_value"));
        this.amount_available_for_investment_field.setLabel(Model.of("Amount Available For Investment"));
        this.amount_available_for_investment_field.add(new ContainerFeedbackBehavior());
        this.amount_available_for_investment_field.setRequired(true);
        this.amount_available_for_investment_field.add(RangeValidator.minimum(0d));
        this.amount_available_for_investment_container.add(this.amount_available_for_investment_field);
        this.amount_available_for_investment_container.newFeedback("amount_available_for_investment_feedback", this.amount_available_for_investment_field);

        this.duration_column = this.row2.newUIColumn("duration_column", Size.Four_4);
        this.duration_container = this.duration_column.newUIContainer("duration_container");
        this.duration_field = new TextField<>("duration_field", new PropertyModel<>(this, "duration_value"));
        this.duration_field.setLabel(Model.of("Duration"));
        this.duration_field.add(new ContainerFeedbackBehavior());
        this.duration_field.add(RangeValidator.minimum(1));
        this.duration_field.setRequired(true);
        this.duration_container.add(this.duration_field);
        this.duration_container.newFeedback("duration_feedback", this.duration_field);

        this.row2.lastUIColumn("last_column");

        this.row3 = UIRow.newUIRow("row3", this.form);

        this.minimum_investment_amount_column = this.row3.newUIColumn("minimum_investment_amount_column", Size.Four_4);
        this.minimum_investment_amount_container = this.minimum_investment_amount_column.newUIContainer("minimum_investment_amount_container");
        this.minimum_investment_amount_field = new TextField<>("minimum_investment_amount_field", new PropertyModel<>(this, "minimum_investment_amount_value"));
        this.minimum_investment_amount_field.setLabel(Model.of("Minimum Investment Amount"));
        this.minimum_investment_amount_field.add(RangeValidator.minimum(0D));
        this.minimum_investment_amount_field.add(new ContainerFeedbackBehavior());
        this.minimum_investment_amount_field.setRequired(true);
        this.minimum_investment_amount_container.add(this.minimum_investment_amount_field);
        this.minimum_investment_amount_container.newFeedback("minimum_investment_amount_feedback", this.minimum_investment_amount_field);

        this.maximum_investment_amount_column = this.row3.newUIColumn("maximum_investment_amount_column", Size.Four_4);
        this.maximum_investment_amount_container = this.maximum_investment_amount_column.newUIContainer("maximum_investment_amount_container");
        this.maximum_investment_amount_field = new TextField<>("maximum_investment_amount_field", new PropertyModel<>(this, "maximum_investment_amount_value"));
        this.maximum_investment_amount_field.setLabel(Model.of("Maximum Investment Amount"));
        this.minimum_investment_amount_field.add(RangeValidator.minimum(0D));
        this.maximum_investment_amount_field.add(new ContainerFeedbackBehavior());
        this.maximum_investment_amount_field.setRequired(true);
        this.maximum_investment_amount_container.add(this.maximum_investment_amount_field);
        this.maximum_investment_amount_container.newFeedback("maximum_investment_amount_feedback", this.maximum_investment_amount_field);

        this.maximum_investment_amount_per_investor_column = this.row3.newUIColumn("maximum_investment_amount_per_investor_column", Size.Four_4);
        this.maximum_investment_amount_per_investor_container = this.maximum_investment_amount_per_investor_column.newUIContainer("maximum_investment_amount_per_investor_container");
        this.maximum_investment_amount_per_investor_field = new TextField<>("maximum_investment_amount_per_investor_field", new PropertyModel<>(this, "maximum_investment_amount_per_investor_value"));
        this.maximum_investment_amount_per_investor_field.setLabel(Model.of("Maximum Investment Amount Per Investor"));
        this.maximum_investment_amount_per_investor_field.add(new ContainerFeedbackBehavior());
        this.maximum_investment_amount_per_investor_field.add(RangeValidator.minimum(0D));
        this.maximum_investment_amount_per_investor_field.setRequired(true);
        this.maximum_investment_amount_per_investor_container.add(this.maximum_investment_amount_per_investor_field);
        this.maximum_investment_amount_per_investor_container.newFeedback("maximum_investment_amount_per_investor_feedback", this.maximum_investment_amount_per_investor_field);

        this.row3.lastUIColumn("last_column");

        this.saveButton = new Button("saveButton") {
            @Override
            public void onSubmit() {
                saveButtonClick();
            }
        };
        this.form.add(this.saveButton);

        this.cancelButton = new BookmarkablePageLink<>("cancelButton", TrancheBrowsePage.class);
        this.form.add(this.cancelButton);
    }

    protected void saveButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        TrancheRepository repository = context.getBean(TrancheRepository.class);

        Tranche entity = new Tranche();
        entity.setName(this.name_value);
        entity.setDuration(this.duration_value);
        entity.setAnnualInterest(this.annual_interest_value);
        entity.setMinimumInvestmentAmount(this.minimum_investment_amount_value);
        entity.setMaximumInvestmentAmount(this.maximum_investment_amount_value);
        entity.setMaximumInvestmentAmountPerInvestor(this.maximum_investment_amount_value);
        entity.setAmountAvailableForInvestment(this.amount_available_for_investment_value);
        repository.save(entity);

        setResponsePage(TrancheBrowsePage.class);
    }

}
