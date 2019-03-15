package com.qrsynergy;

import javax.servlet.annotation.WebServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.qrsynergy.domain.ConstURL;
import com.qrsynergy.domain.LoginResponse;
import com.qrsynergy.domain.user.UserRepository;
import com.qrsynergy.event.DashboardEvent;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.qrsynergy.event.DashboardEventBus;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.qrsynergy.event.DashboardEvent.BrowserResizeEvent;
import com.qrsynergy.event.DashboardEvent.CloseOpenWindowsEvent;
import com.qrsynergy.event.DashboardEvent.UserLoggedOutEvent;
import com.qrsynergy.event.DashboardEvent.UserLoginRequestedEvent;
import com.qrsynergy.event.DashboardEvent.WrongLoginEvent;
import com.qrsynergy.domain.user.User;
import com.qrsynergy.view.LoginView;
import com.qrsynergy.view.MainView;
import com.vaadin.ui.Window;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
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
@Widgetset("com.qrsynergy.MyAppWidgetset")
public final class DashboardUI extends UI {


    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();
    @Autowired
    private UserRepository userRepository;


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

        User user = userRepository.findByUsername(event.getUserName());

        System.out.println(user.getEmail());
        /*
        // parse it to the JSON object
        JSONObject json = new JSONObject();
        json.put("username", event.getUserName());
        json.put("password", event.getPassword());

        // prepare HTTP request
        HttpClient httpClient = HttpClientBuilder.create().build();
        try{
            HttpPost request = new HttpPost(ConstURL.loginURL);
            HttpResponse response;

            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            response = httpClient.execute(request);

            int code = response.getStatusLine().getStatusCode();
            // successful request
            if(code == 200){
                ObjectMapper objectMapper = new ObjectMapper();
                LoginResponse loginResponse = objectMapper.readValue(response.getEntity().getContent(), LoginResponse.class);

                // store it in session
                VaadinSession.getCurrent().setAttribute("token", loginResponse.getToken());
                VaadinSession.getCurrent().setAttribute(User.class.getName(),loginResponse.getUser());

                // update view to redirect dashboard
                // updateContent();
            }
            else if(code == 401){
                // TODO
                // it is not fired
                DashboardEventBus.post(new WrongLoginEvent("Incorrect username or password"));
            }

        }catch (Exception ex){
            // TODO
            // it is not fired
            System.out.println("Exception: " + ex);
            DashboardEventBus.post(new WrongLoginEvent("Internal server error"));
        }
        */

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


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DashboardUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
