package com.qrsynergy.ui.view.viewdocument.tabs;

import com.qrsynergy.model.QR;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.vaadin.ui.*;
import com.qrsynergy.ui.view.helper.qrgenerator.QRGenerator;

/**
 * Details tab
 * Generates content from the given QR given in the constructor
 */
public class Details {

    HorizontalLayout content;
    QR qr;

    /**
     * Constructor
     * @param qr QR to be rendered
     */
    public Details(QR qr){
        this.qr = qr;
    }

    /**
     * Creates 2 column,
     * The first one contains static information,
     * The second one contains QR Image and download button
     * @return content of the details tab
     */
    public Component getContent(){
        content = new HorizontalLayout();
        content.setSizeFull();

        // first column, contains static details
        VerticalLayout infoColumn = new VerticalLayout();
        infoColumn.addComponent(buildInfoColumn());

        // Second column, contains qr image and download button
        VerticalLayout qrImageColumn = new VerticalLayout();
        qrImageColumn.addStyleNames("text-center", "slot-text-center");
        qrImageColumn.addComponent(buildQRImageColumn());

        content.addComponents(infoColumn, qrImageColumn);
        return content;
    }


    /**
     * Builds static information column
     * @return layout contains static information about QR
     */
    private Component buildInfoColumn(){
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleNames("text-center", "slot-text-center");

        Label url = new Label("ID: " + qr.getUrl());
        Label originalName = new Label("Original name: " + qr.getOriginalName());

        Label isPublished = new Label("Publish: " +
                (qr.getPublished() ? "Yes" : "No")
        );

        Label documentType = new Label("Document type: " + qr.getDocumentType());
        Label creationDate = new Label("Creation date: " + qr.getCreationDate());
        Label expirationDate = new Label("Expiration date: " + qr.getExpirationDate());

        layout.addComponents(
                url,
                originalName,
                isPublished,
                documentType,
                creationDate,
                expirationDate);
        return layout;
    }

    /**
     * Builds QR image nad download button column
     * @return component containin qr and download button
     */
    private Component buildQRImageColumn(){

        VerticalLayout layout;
        try{
            layout = (VerticalLayout) QRGenerator.generateQRandDownloadButton(qr);
        }
        catch(Exception e)
        {
            layout = new VerticalLayout();
            ShowNotification.showNotification("There is an error during loading the QR image");
        }
        return layout;
    }
}
