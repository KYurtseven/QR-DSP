package com.qrsynergy.ui.view.helper.qrgenerator;

import ch.qos.logback.core.CoreConstants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qrsynergy.model.QR;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Static qr generation class
 */
public class QRGenerator {

    public static final ProductionMode productionMode = ProductionMode.LOCAL;


    public static String getQRViewApiUrl(){
        if(productionMode.equals(ProductionMode.LOCAL)){
            return "http:localhost:8080/api/qr/view/";
        }
        else if(productionMode.equals(ProductionMode.PRODUCTION)){
            return "TODO HERE";
        }
        else{
            // TEST
            return "TODO2 here";
        }
    }

    /**
     * Generates byte array for reaching API.
     * Transforms byte array to stream resource, it will be rendered in the screen
     * @param qr QR object to be rendered
     * @param width width of the QR
     * @param height height of the QR
     * @return stream resource to be added to the UI
     * @throws WriterException writer exception
     * @throws IOException io exception
     */
    public static StreamResource generateQR(QR qr, int width, int height) throws WriterException, IOException{
        // api url
        String api_url = getQRViewApiUrl() + qr.getUrl();
        // generate byte array
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(api_url, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        // transform byte array to stream resource

        StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                return new ByteArrayInputStream(pngData);
            }
        }, qr.getOriginalName() + ".png");

        return resource;
    }

    /**
     * Generates QR and Download button component
     *
     * @param qr QR
     * @param width width
     * @param height heigth
     * @return vertical layout that contains QR and button
     * @throws WriterException exception
     * @throws IOException exception
     */
    public static Component generateQRandDownloadButton(QR qr, int width, int height) throws WriterException, IOException{
        StreamResource streamResource = generateQR(qr, width, height);
        Image qrImage = new Image("", streamResource);
        qrImage.addStyleNames("text-center", "slot-text-center");

        VerticalLayout layout = new VerticalLayout();
        layout.setId("qr_generator_layout");
        layout.setSizeFull();
        layout.addStyleNames("text-center", "slot-text-center", "padding-top-0");

        layout.addComponents(qrImage, buildDownloadButton(streamResource, width));

        return layout;
    }

    /**
     * Generates QR and Download button component
     * Default Size is 200*200
     * @param qr QR
     * @return vertical layout that contains QR and button
     * @throws WriterException
     * @throws IOException
     */
    public static Component generateQRandDownloadButton(QR qr) throws  WriterException, IOException{
        return generateQRandDownloadButton(qr, 200, 200);
    }

    /**
     * TODO, to be deprecated
     * @param qr
     */
    public static void showGeneratedQR(QR qr) {
        Window subWindow = new Window("QR Image");
        VerticalLayout windowLayout = new VerticalLayout();

        Label urlField = new Label("ID: " + qr.getUrl());
        StreamResource image;
        try {
            image = generateQR(qr, 200, 200);

            windowLayout.addComponent(urlField);
            windowLayout.addComponent(new Image("", image));
        }
        catch (Exception e)
        {
            Notification notification = new Notification(e.getMessage());
            notification.setDelayMsec(2000);
            notification.setPosition(Position.BOTTOM_RIGHT);
            notification.show(Page.getCurrent());
            return;
        }

        windowLayout.addComponent(buildDownloadButton(image, 200));

        subWindow.setWidth(350, Sizeable.Unit.PIXELS);
        subWindow.setHeight(380, Sizeable.Unit.PIXELS);
        subWindow.setContent(windowLayout);
        subWindow.center();
        subWindow.setModal(true);
        UI.getCurrent().addWindow(subWindow);
    }

    /**
     * helper download button builder
     * @return download button of the qr
     */
    private static Component buildDownloadButton(StreamResource image, int width){
        Button downloadButton = new Button();
        downloadButton.setIcon(VaadinIcons.DOWNLOAD);
        downloadButton.setWidth((width-50) + "px");
        downloadButton.addStyleNames("text-center", "slot-text-center");

        FileDownloader fileDownloader = new FileDownloader(image);
        fileDownloader.extend(downloadButton);
        return downloadButton;
    }
}
