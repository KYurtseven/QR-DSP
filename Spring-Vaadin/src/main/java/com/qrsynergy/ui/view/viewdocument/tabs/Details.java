package com.qrsynergy.ui.view.viewdocument.tabs;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.vaadin.addon.onoffswitch.OnOffSwitch;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.qrsynergy.ui.view.helper.qrgenerator.QRGenerator;

/**
 * Details tab
 * Generates content from the given QR given in the constructor
 */
public class Details {

    private User user;
    private HorizontalLayout content;
    private QR qr;

    /**
     * Constructor
     * @param qr QR to be rendered
     */
    public Details(QR qr){
        this.qr = qr;
        this.user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
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
     * If the user is owner or editor of the document,
     * he can change the name of the document. The owner/editor can see
     * a text field for changing the name and a submit button for saving the changes.
     * Other users can only see name of the document.
     * @return either editable textfield and button, or name
     */
    private Component renderChangeDocumentName(){

        RightType rightType = qr.findUsersRightInQR(user.getEmail());
        // This user is the owner or editor, can edit the document name field
        if(rightType == RightType.EDIT || rightType == RightType.OWNER){
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            String name = qr.getOriginalName().substring(0, qr.getOriginalName().lastIndexOf("."));
            String type = qr.getOriginalName().substring(qr.getOriginalName().lastIndexOf(".") +1);

            TextField textField = new TextField();
            textField.setValue(name);

            Label nameLabel = new Label("Name: ");
            nameLabel.setStyleName("margin-top-15");

            Label extensionLabel = new Label("." + type);
            extensionLabel.setStyleName("margin-top-15");

            Button changeNameButton = new Button("Save");

            changeNameButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    // disable button
                    changeNameButton.setEnabled(false);
                    // validity check
                    if(textField.getValue() == null || textField.getValue().length() == 0){
                        ShowNotification.showWarningNotification("Please enter valid document name");
                    }
                    // update QR and user's UserQR
                    boolean result = ((DashboardUI) UI.getCurrent()).qrService.
                            changeQRName(qr, textField.getValue() + "." + type);

                    if(result){
                        ShowNotification.showNotification("Successfully changed");
                    }
                    else{
                        ShowNotification.showWarningNotification("There is an error during name changing");
                    }
                    // enable it for future editing
                    changeNameButton.setEnabled(true);
                }
            });

            horizontalLayout.addComponents(nameLabel, textField, extensionLabel, changeNameButton);
            return horizontalLayout;
        }
        Label defaultLabel =  new Label("Name: " + qr.getOriginalName());
        return defaultLabel;
    }

    /**
     * Renders publish switch if the user is the owner of the document
     * @return component containing switch for publishing
     */
    private Component publishDocumentSwitch(){

        HorizontalLayout publishLayout = new HorizontalLayout();
        Label isPublishedLabel = new Label("Publish");

        OnOffSwitch publishSwitch = new OnOffSwitch(qr.getPublished());
        if(publishSwitch.getValue())
            publishSwitch.setReadOnly(true);
        else
        {
            publishSwitch.addValueChangeListener(event -> {
                if(event.getValue())
                {
                    boolean result = ((DashboardUI) UI.getCurrent()).qrService.publishQR(qr);
                    if(result){
                        publishSwitch.setReadOnly(true);
                        ShowNotification.showNotification("QR is published");
                    }
                    else{
                        ShowNotification.showWarningNotification("An error occured while publishing the document");
                    }
                }
            });
        }
        publishLayout.addComponents(isPublishedLabel, publishSwitch);
        return publishLayout;
    }


    /**
     * Builds static information column
     * @return layout contains static information about QR
     */
    private Component buildInfoColumn(){
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleNames("text-center", "slot-text-center");

        // url row
        Label url = new Label("ID: " + qr.getUrl());

        // add url to layout and add change document name to layout
        layout.addComponents(url, renderChangeDocumentName());

        // switch row
        RightType rightType = qr.findUsersRightInQR(user.getEmail());
        if(rightType == RightType.OWNER)
            layout.addComponent(publishDocumentSwitch());

        // others
        Label documentType = new Label("Document type: " + qr.getDocumentType());
        Label creationDate = new Label("Creation date: " + qr.getCreationDate());
        Label expirationDate = new Label("Expiration date: " + qr.getExpirationDate());

        layout.addComponents(documentType, creationDate, expirationDate);
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
