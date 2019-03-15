package com.qrsynergy.ui.view.createdocument;

import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.wizards.Wizard;


@SuppressWarnings("serial")
public final class CreateDocumentView extends Panel implements View{


    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";

    private Wizard wizard;

    private Label titleLabel;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;

    public CreateDocumentView(){
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setSpacing(false);
        // TODO
        // generate style name generate qr view
        root.addStyleName("sales");

        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel = new Label("Generate New QR");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        // TODO
        // add contents here!
        wizard = new Wizard();
        wizard.addStep(new UploadFileStep());

        dashboardPanels.addComponent(wizard);
        return dashboardPanels;
    }
}
