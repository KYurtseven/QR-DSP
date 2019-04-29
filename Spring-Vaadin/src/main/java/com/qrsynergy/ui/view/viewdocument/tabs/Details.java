package com.qrsynergy.ui.view.viewdocument.tabs;

import com.qrsynergy.model.QR;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Details {

    /**
     * TODO
     * Better UI
     * @param qr QR
     * @return component to be rendered in the tab
     */
    public static Component qrInfoTab(QR qr){
        VerticalLayout layout = new VerticalLayout();

        Label url = new Label("URL: " + qr.getUrl());
        Label originalName = new Label("Original name: " + qr.getOriginalName());

        Label isPublished = new Label("Publish: " +
                (qr.getPublished() ? "Yes" : "No")
        );

        Label documentType = new Label("Document type: " + qr.getDocumentType());
        Label creationDate = new Label("Creation date: " + qr.getCreationDate());
        Label expirationDate = new Label("Expiration date: " + qr.getExpirationDate());

        layout.addComponents(url, originalName, isPublished, documentType, creationDate, expirationDate);
        return layout;
    }
}