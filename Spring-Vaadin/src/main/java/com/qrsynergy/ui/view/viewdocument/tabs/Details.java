package com.qrsynergy.ui.view.viewdocument.tabs;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.vaadin.server.VaadinSession;
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
     *
     * @return
     */
    private Component renderChangeDocumentName(){
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        RightType rightType = qr.findUsersRightInQR(user.getEmail());
        // This user is the owner or editor, can edit the document name field
        if(rightType == RightType.EDIT || rightType == RightType.OWNER){
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            String name = qr.getOriginalName().substring(0, qr.getOriginalName().lastIndexOf("."));
            String type = qr.getOriginalName().substring(qr.getOriginalName().lastIndexOf(".") +1);

            TextField textField = new TextField();
            textField.setValue(name);

            Label extensionLabel = new Label("." + type);
            extensionLabel.setStyleName("margin-top-25");

            Button changeNameButton = new Button("Save");

            changeNameButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {

                    if(textField.getValue() == null || textField.getValue().length() == 0){
                        ShowNotification.showWarningNotification("Please enter valid document name");
                    }
                    boolean result = ((DashboardUI) UI.getCurrent()).qrService.
                            changeQRName(qr, textField.getValue() + "." + type);

                    if(result){
                        ShowNotification.showNotification("Successfully changed");
                    }
                    else{
                        ShowNotification.showWarningNotification("There is an error during name changing");
                    }
                }
            });

            horizontalLayout.addComponents(textField, extensionLabel, changeNameButton);
            return horizontalLayout;
        }
        Label defaultLabel =  new Label(qr.getOriginalName());
        return defaultLabel;
    }


    /**
     * Builds static information column
     * @return layout contains static information about QR
     */
    private Component buildInfoColumn(){
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleNames("text-center", "slot-text-center");

        Label url = new Label("ID: " + qr.getUrl());

        HorizontalLayout nameLayout = new HorizontalLayout();
        Label nameLabel = new Label("Name: ");
        nameLayout.addComponents(nameLabel, renderChangeDocumentName());

        Label isPublished = new Label("Publish: " +
                (qr.getPublished() ? "Yes" : "No")
        );

        Label documentType = new Label("Document type: " + qr.getDocumentType());
        Label creationDate = new Label("Creation date: " + qr.getCreationDate());
        Label expirationDate = new Label("Expiration date: " + qr.getExpirationDate());

        layout.addComponents(
                url,
                nameLayout,
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
