package com.senior.kilde.assignment.web.pages;

import com.senior.kilde.assignment.web.factory.WebSession;
import com.senior.kilde.assignment.web.provider.MemoryFooterProvider;
import com.senior.kilde.assignment.web.provider.MemoryMainSidebarProvider;
import com.senior.kilde.assignment.web.provider.MemoryThemeProvider;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class MasterPage extends com.senior.cyber.frmk.common.wicket.layout.MasterPage {

    public MasterPage() {
    }

    public MasterPage(IModel<?> model) {
        super(model);
    }

    public MasterPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitData() {
        this.pageTitle = "Kilde";
        this.mainSidebarProvider = new MemoryMainSidebarProvider(getSession(), this);
        this.footerProvider = new MemoryFooterProvider();
        this.themeProvider = new MemoryThemeProvider();
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
    }

    @Override
    public WebSession getSession() {
        return (WebSession) super.getSession();
    }

}
