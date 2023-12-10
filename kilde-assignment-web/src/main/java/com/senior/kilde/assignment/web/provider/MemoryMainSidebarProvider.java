package com.senior.kilde.assignment.web.provider;

import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.model.Brand;
import com.senior.cyber.frmk.common.model.MainSidebar;
import com.senior.cyber.frmk.common.model.UserPanel;
import com.senior.cyber.frmk.common.model.menu.sidebar.SidebarMenu;
import com.senior.cyber.frmk.common.model.menu.sidebar.SidebarMenuDropdown;
import com.senior.cyber.frmk.common.model.menu.sidebar.SidebarMenuItem;
import com.senior.cyber.frmk.common.provider.IMainSidebarProvider;
import com.senior.kilde.assignment.dao.entity.Role;
import com.senior.kilde.assignment.dao.entity.User;
import com.senior.kilde.assignment.dao.repository.UserRepository;
import com.senior.kilde.assignment.web.WebApplication;
import com.senior.kilde.assignment.web.factory.WebSession;
import com.senior.kilde.assignment.web.pages.LogoutPage;
import com.senior.kilde.assignment.web.pages.borrower.BorrowerBrowsePage;
import com.senior.kilde.assignment.web.pages.group.GroupBrowsePage;
import com.senior.kilde.assignment.web.pages.investor.InvestorBrowsePage;
import com.senior.kilde.assignment.web.pages.my.profile.MyProfilePage;
import com.senior.kilde.assignment.web.pages.role.RoleBrowsePage;
import com.senior.kilde.assignment.web.pages.tranche.TrancheBrowsePage;
import com.senior.kilde.assignment.web.pages.tranche.TrancheCreatePage;
import com.senior.kilde.assignment.web.pages.user.UserBrowsePage;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class MemoryMainSidebarProvider implements IMainSidebarProvider {

    private final WebSession session;

    private final Page currentPage;

    public MemoryMainSidebarProvider(WebSession session, Page currentPage) {
        this.session = session;
        this.currentPage = currentPage;
    }

    @Override
    public MainSidebar fetchMainSidebar() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository repository = context.getBean(UserRepository.class);

        Roles roles = this.session.getRoles();

        User user = null;
        if (this.session.getUserId() != null) {
            user = repository.findById(this.session.getUserId()).orElseThrow(() -> new RuntimeException("user not found"));
        } else {
            user = new User();
        }

        Brand brand = new Brand("Kilde", new PackageResourceReference(WebApplication.class, "logo.png"), (Class<? extends WebPage>) org.apache.wicket.protocol.http.WebApplication.get().getHomePage());
        UserPanel userPanel = new UserPanel(new PackageResourceReference(WebApplication.class, "user.png"), user.getLogin(), MyProfilePage.class);
        List<SidebarMenu> children = new ArrayList<>();

        if (this.session.isSignedIn()) {
            List<SidebarMenu> securityMenu = securityMenu(roles);
            if (!securityMenu.isEmpty()) {
                children.add(new SidebarMenuDropdown("fas fas fa-lock", "Security", null, securityMenu));
            }
        }

        if (this.session.isSignedIn()) {
            children.add(new SidebarMenuItem("fas fa-user-tie", "Investor", null, InvestorBrowsePage.class));
            children.add(new SidebarMenuItem("fas fa-user-tie", "Borrower", null, BorrowerBrowsePage.class));
            List<SidebarMenu> trancheMenu = trancheMenu(roles);
            if (!trancheMenu.isEmpty()) {
                children.add(new SidebarMenuDropdown("fas fa-user-tie", "Tranche", null, trancheMenu));
            }
        }

        if (this.session.isSignedIn()) {
            children.add(new SidebarMenuItem("fas fa-sign-out-alt", "Logout", null, LogoutPage.class));
        }

        MainSidebar mainSidebar = new MainSidebar();
        mainSidebar.setBrand(brand);
        mainSidebar.setUserPanel(userPanel);
        mainSidebar.setSidebarMenu(children);
        mainSidebar.setSearchable(false);
        return mainSidebar;
    }

    protected List<SidebarMenu> securityMenu(Roles roles) {
        List<SidebarMenu> children = new ArrayList<>();
        if (roles.hasRole(Role.NAME_ROOT) || roles.hasRole(Role.NAME_ROOT)) {
            children.add(new SidebarMenuItem("fas fa-users", "Group", null, GroupBrowsePage.class));
        }
        if (roles.hasRole(Role.NAME_ROOT) || roles.hasRole(Role.NAME_ROOT)) {
            children.add(new SidebarMenuItem("fas fa-user", "User", null, UserBrowsePage.class));
        }
        if (roles.hasRole(Role.NAME_ROOT) || roles.hasRole(Role.NAME_ROOT)) {
            children.add(new SidebarMenuItem("fas fa-universal-access", "Role", null, RoleBrowsePage.class));
        }
        return children;
    }

    protected List<SidebarMenu> trancheMenu(Roles roles) {
        List<SidebarMenu> children = new ArrayList<>();
        if (roles.hasRole(Role.NAME_ROOT) || roles.hasRole(Role.NAME_ROOT)) {
            children.add(new SidebarMenuItem("fas fa-plus", "Create", null, TrancheCreatePage.class));
        }
        if (roles.hasRole(Role.NAME_ROOT) || roles.hasRole(Role.NAME_ROOT)) {
            children.add(new SidebarMenuItem("fas fa-table", "Browse", null, TrancheBrowsePage.class));
        }
//        if (roles.hasRole(Role.NAME_ROOT) || roles.hasRole(Role.NAME_ROOT)) {
//            children.add(new SidebarMenuItem("fas fa-pen", "Role", null, RoleBrowsePage.class));
//        }
        return children;
    }

}
