package com.qrsynergy.ui.view;

import com.qrsynergy.model.Company;
import com.qrsynergy.service.CompanyService;
import com.qrsynergy.ui.DashboardNavigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;


/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {


    public MainView() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);

        addComponent(new DashboardMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleNames("view-content","overflow-auto");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new DashboardNavigator(content);
    }
}
