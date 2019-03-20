package com.qrsynergy.ui.view.viewdocument;

import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public final class ViewDocumentView extends Panel implements  View{

    public static final String TITLE_ID = "viewdocument-title";
    private final VerticalLayout root;

    public ViewDocumentView(){
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

        Label titleLabel = new Label("View Documents");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    /**
     * TODO
     * Document will be viewed under this function!
     * @return
     */
    private Component buildContent(){
        Label newLabel = new Label("Write your code here and below!");

        return newLabel;
    }
}
