package com.qrsynergy.ui.view.viewdocument;

import com.qrsynergy.model.User;
import com.qrsynergy.model.UserDocument;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.MyUI;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.server.StreamResource;

import java.io.IOException;
import java.util.List;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import sun.misc.BASE64Decoder;



import com.vaadin.server.BrowserWindowOpener;


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
        root.addComponent(buildContent());
        //root.setExpandRatio(content, 1);

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
        VerticalLayout content = new VerticalLayout();

        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());

        UserQR userQR = ((DashboardUI) UI.getCurrent()).userQRService.getUserQR(user);

        Label ownLabel = new Label("Owned documents");
        content.addComponent(buildQRItems(userQR.getO_docs()));

        return content;
    }

    private Component buildQRItems(List<UserDocument> userDocuments){
        HorizontalLayout row = new HorizontalLayout();

        for (UserDocument userDocument: userDocuments) {
            VerticalLayout column = new VerticalLayout();
            // TODO
            // push image
            BufferedImage image = null;
            byte[] imageByte;
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                imageByte = decoder.decodeBuffer("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");
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByte);
                image = ImageIO.read(byteArrayInputStream);
                byteArrayInputStream.close();
                StreamResource resource = new StreamResource(
                        new StreamResource.StreamSource() {
                            @Override
                            public InputStream getStream() {
                                return new ByteArrayInputStream(imageByte);
                            }
                        }, "filename.png");

                column.addComponent(new Image("",resource));
            }
            catch (IOException e)
            {
            }


            // create horizontal layout
            HorizontalLayout nameAndButton = new HorizontalLayout();
            // push name and button to horizontal layout
            Label qrName = new Label(userDocument.getName());
            Button openDocument = new Button("Open");
            BrowserWindowOpener opener = new BrowserWindowOpener(MyUI.class);
            //opener.setFeatures( "height=600,width=900,resizable" ); //use this to make pop-up
            opener.extend(openDocument);
            opener.setParameter("qr_id", userDocument.getUrl());

            nameAndButton.addComponents(qrName, openDocument);
            column.addComponent(nameAndButton);
            row.addComponent(column);
        }

        return row;
    }


    //TODO Implement this: Find qr by id and get image from there.
    /*
    private void showQrImage() throws IOException {

        QR document = QRRepository.findByUrl();

        BufferedImage image = null;
        byte[] imageByte;

        BASE64Decoder decoder = new BASE64Decoder();
        imageByte = decoder.decodeBuffer(document.getUrl());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByte);
        image = ImageIO.read(byteArrayInputStream);
        byteArrayInputStream.close();

        StreamResource resource = new StreamResource(
                new StreamResource.StreamSource() {
                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(imageByte);
                    }
                }, "filename.png");


        root.addComponent(new Image("", resource));
    }*/
}
