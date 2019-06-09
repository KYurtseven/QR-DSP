package com.qrsynergy.ui.view;

import com.qrsynergy.GlobalSettings;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.event.DashboardEvent;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.*;
import com.vaadin.ui.*;

import java.io.File;

public class DownloadApp extends VerticalLayout implements View {

    /**
     * Constructor
     */
    public DownloadApp() {
        // set UI contents
        setSizeFull();
        setMargin(false);
        setSpacing(false);

        DashboardEventBus.register(this);

        Component downloadAppLayout = downloadAppLayout();
        addComponent(downloadAppLayout);
        setComponentAlignment(downloadAppLayout, Alignment.MIDDLE_CENTER);

    }



    private Component downloadAppLayout(){
        final VerticalLayout downloadAppPanel = new VerticalLayout();
        downloadAppPanel.setSizeUndefined();
        downloadAppPanel.setMargin(false);
        Responsive.makeResponsive(downloadAppPanel);
        downloadAppPanel.addStyleName("login-panel");

        downloadAppPanel.addComponent(buildFields());
        return downloadAppPanel;
    }


    /**
     * Builds all fields
     * @return all fields
     */
    private Component buildFields(){
        VerticalLayout fields = new VerticalLayout();
        fields.addStyleName("fields");

        fields.addComponents(
                buildBackToLoginButton(),
                buildImageAndSubmit()
        );

        return fields;
    }

    /**
     *
     * @return
     */
    private Component buildImageAndSubmit(){
        VerticalLayout vl = new VerticalLayout();

        FileResource resource = new FileResource(new File(GlobalSettings.getImgLocation() + "qr.png"));
        Image qrImage = new Image("", resource);

        Button downloadButton = new Button("Download");
        // set resource
        Resource res = new FileResource(new File(GlobalSettings.getMobileAppLocation()));
        FileDownloader fd = new FileDownloader(res);
        fd.extend(downloadButton);

        vl.addComponents(qrImage, downloadButton);
        return vl;
    }


    /**
     * Builds button that returns user to the login page
     * @return button
     */
    private Component buildBackToLoginButton(){
        Button button = new Button(" To the previous page");
        button.setIcon(VaadinIcons.BACKSPACE);
        button.setSizeFull();
        button.setHeight("30px");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                DashboardEventBus.post(new DashboardEvent.UserDownloadMobileAppFinishedEvent());
            }
        });
        return button;
    }

}
