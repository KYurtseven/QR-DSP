package com.qrsynergy.ui.view.qrgenerator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class qrgenerator {


    public qrgenerator() {
    }

    /**
     * Function that generates qr with given inputs and
     * stores it in a byte array.
     *
     * @param qrID qrID
     * @param width width of qr image
     * @param height height of qr image
     * @return
     */
    private static byte[] generateQRCode(String qrID, int width, int height) throws WriterException, IOException
    {
        String qr_text = "http:localhost/viewqr/" + qrID;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qr_text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }

    private static StreamResource byteArrayToImage(byte[] imageData) throws IOException
    {
        StreamResource resource = new StreamResource(
                new StreamResource.StreamSource() {
                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(imageData);
                    }
                }, "filename.png");

        return resource;
    }

    public static void showGeneratedQR(String qrID)
    {
        Window subWindow = new Window("QR Image");
        VerticalLayout windowLayout = new VerticalLayout();

        try {
            Label urlField = new Label(qrID);

            byte[] imageData =  generateQRCode(qrID, 200,200);
            StreamResource image = null;
            image = byteArrayToImage(imageData);

            windowLayout.addComponent(urlField);
            windowLayout.addComponent(new Image("", image));
        }
        catch (Exception e)
        {

        }

        Button print = new Button();
        print.setIcon(VaadinIcons.PRINT);
        print.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                JavaScript.getCurrent().execute("print();");
            }
        });

        windowLayout.addComponent(print);

        subWindow.setWidth(350, Sizeable.Unit.PIXELS);
        subWindow.setHeight(380, Sizeable.Unit.PIXELS);
        subWindow.setContent(windowLayout);
        subWindow.center();
        subWindow.setModal(true);
        UI.getCurrent().addWindow(subWindow);

    }
}
