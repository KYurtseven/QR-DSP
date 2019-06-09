package com.qrsynergy.ui;

import javax.servlet.annotation.WebServlet;

import com.google.common.eventbus.Subscribe;
import com.qrsynergy.model.Company;
import com.qrsynergy.model.User;
import com.qrsynergy.service.*;
import com.qrsynergy.ui.event.DashboardEvent;
import com.qrsynergy.ui.view.*;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.qrsynergy.ui.event.DashboardEvent.BrowserResizeEvent;
import com.qrsynergy.ui.event.DashboardEvent.CloseOpenWindowsEvent;
import com.qrsynergy.ui.event.DashboardEvent.UserLoggedOutEvent;
import com.qrsynergy.ui.event.DashboardEvent.UserLoginRequestedEvent;
import com.qrsynergy.ui.event.DashboardEvent.CompanyCreateRequestedEvent;
import com.qrsynergy.ui.event.DashboardEvent.UserSignUpRequestedEvent;
import com.qrsynergy.ui.event.DashboardEvent.UserSignUpFinishedEvent;
import com.qrsynergy.ui.event.DashboardEvent.ExcelPageRequestedEvent;
import com.qrsynergy.ui.event.DashboardEvent.ExcelPreviousPageEvent;
import com.qrsynergy.ui.event.DashboardEvent.UserDownloadMobileAppPageRequestedEvent;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("dashboard")
@Title("QR Synergy")
//@Widgetset("com.qrsynergy.MyAppWidgetset")
@SpringUI
public final class DashboardUI extends UI {

    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

    @Autowired
    public UserService userService;
    @Autowired
    public CompanyService companyService;
    @Autowired
    public QRService qrService;
    @Autowired
    public UserQRService userQRService;
    @Autowired
    public CommentService commentService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setLocale(Locale.US);

        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        Page.getCurrent().addBrowserWindowResizeListener(
            new BrowserWindowResizeListener() {
                @Override
                public void browserWindowResized(
                        final BrowserWindowResizeEvent event) {
                    DashboardEventBus.post(new BrowserResizeEvent());
                }
            });
    }

    /**
     * After pressing submit button, re-render the scene
     * If there is a user(successful login), let him to see Dashboard
     */
    private void updateContent(){
        User user = (User) VaadinSession.getCurrent()
                .getAttribute(User.class.getName());
        if (user != null) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    /**
     * When the user presses submit button, event is fired
     * @param event
     */
    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {
/*
        //Local case for speeding up

        User user = userService.findByEmail("koray.can.yurtseven@ford.com");

        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
*/

        User user = userService.findByEmail(event.getEmail());
        if(user != null){
            if(user.isCorrectPassword(event.getPassword())){
                VaadinSession.getCurrent().setAttribute(User.class.getName(),user);
                updateContent();
            }
            else{
                ShowNotification.showNotification("Please enter correct credentials");
            }
        }
        else{
            ShowNotification.showNotification("Please enter correct credentials");
        }

    }


    @Subscribe
    public void userDownloadMobileAppFinished(final DashboardEvent.UserDownloadMobileAppFinishedEvent event){
        updateContent();
    }

    @Subscribe
    public void userSignUpRequested(final UserSignUpRequestedEvent event){
        setContent(new SignUpView());
        addStyleName("loginview");
    }

    @Subscribe
    public void userDownloadAppPageRequested(final UserDownloadMobileAppPageRequestedEvent event){
        setContent(new DownloadApp());
        addStyleNames("loginview");
    }


    @Subscribe
    public void userSignUpFinished(final UserSignUpFinishedEvent event){
        updateContent();
    }

    /**
     * Saves company to the database
     * Not used in the production
     * Save occurs from DashboardView component
     * @param event
     */
    @Subscribe
    public void companyCreateRequested(final CompanyCreateRequestedEvent event){

        Company company = new Company(event.getName(), event.getEmailExtension());
        companyService.saveCompany(company);
    }

    /**
     * Close session when logout button is pressed
     * @param event
     */
    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    /**
     * When the excel UI wants to be opened
     * initialize the view
     * @param event
     */
    @Subscribe
    public void excelPageRequested(final ExcelPageRequestedEvent event){
        User user = (User) VaadinSession.getCurrent()
                .getAttribute(User.class.getName());
        setContent(new ExcelView(event.getUrl(), user));
    }

    @Subscribe
    public void excelPreviousPage(final ExcelPreviousPageEvent event){
        updateContent();
    }

    /**
     * To reach DashboardEventBus object from other views
     * @return
     */
    public static DashboardEventBus getDashboardEventbus() {
        return ((DashboardUI) getCurrent()).dashboardEventbus;
    }


    @WebServlet(urlPatterns = "/*",  asyncSupported = true)
    @VaadinServletConfiguration(ui = DashboardUI.class, productionMode = true/*, widgetset = "com.qrsynergy.MyAppWidgetset"*/)
    public static class MyUIServlet extends VaadinServlet {
    }

}
