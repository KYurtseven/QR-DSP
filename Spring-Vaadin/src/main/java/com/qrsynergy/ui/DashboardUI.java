package com.qrsynergy.ui;

import javax.servlet.annotation.WebServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.qrsynergy.model.Company;
import com.qrsynergy.model.User;
import com.qrsynergy.service.CompanyService;
import com.qrsynergy.service.QRService;
import com.qrsynergy.service.UserService;
import com.qrsynergy.ui.event.DashboardEvent;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
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
import com.qrsynergy.ui.view.LoginView;
import com.qrsynergy.ui.view.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.validation.ValidatorAdapter;

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
        // TODO
        // Implement error message when the email or password is not correct

        // TODO
        // For speeding up the test, user validation is bypassed
        // remove it
        User user = userService.findByEmail("koray@gmail.com");

        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
        /*
        User user = userService.findByEmail(event.getEmail());
        if(user != null){
            // TODO
            // check hashed values for comparison
            if(user.getPassword().equals(event.getPassword())){
                VaadinSession.getCurrent().setAttribute(User.class.getName(),user);
                updateContent();
            }
        }
        */
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
