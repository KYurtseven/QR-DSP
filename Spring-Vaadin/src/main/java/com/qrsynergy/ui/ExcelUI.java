package com.qrsynergy.ui;

import java.io.*;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import com.vaadin.ui.*;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;


@Theme("mytheme")
public class ExcelUI extends UI{

    private Spreadsheet spreadsheet = null;
    private String qr_id;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);

        qr_id = vaadinRequest.getParameter( "qr_id" );

        initSpreadsheet();
        layout.addComponent(spreadsheet);
        layout.setExpandRatio(spreadsheet, 1);

        Button saveButton = new Button("Save");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    /*
                    String path = "C:\\Users\\uguro\\Desktop\\qrdsp_last_version\\QR-DSP\\Spring-Vaadin\\FILES\\";
                    path += qr_id + ".xlsx";

                    Workbook workbook = spreadsheet.getWorkbook();

                    File file = new File(path);

                    FileOutputStream fos = new FileOutputStream(file, false);
                    workbook.write(fos);
                    fos.close();
                    */

                    // TODO
                    String path = "C:\\Users\\uguro\\Desktop\\qrdsp_last_version\\QR-DSP\\Spring-Vaadin\\FILES\\";
                    path += qr_id + ".xlsx";

                    spreadsheet.write(path);

                    //spreadsheet.read(new FileInputStream("C:\\Users\\uguro\\Desktop\\qrdsp_last_version\\QR-DSP\\Spring-Vaadin\\FILES\\"));
                    //System.out.println("QRID: " + qr_id);
                }
                catch (FileNotFoundException fn) {
                    System.out.println("File not found");
                }
                catch(IOException e){
                    System.out.println("io exception: " + e);
                }
            }
        });

        layout.addComponent(saveButton);
    }

    private void initSpreadsheet() {
        //File sampleFile = new File("C:\\Users\\uguro\\Desktop\\qrdsp_last_version\\QR-DSP\\Spring-Vaadin\\FILES\\" + qr_id + ".xlsx");
        File sampleFile = new File("D:\\QRDSP\\github\\Spring-Vaadin\\FILES\\" + qr_id + ".xlsx");
        try {
            spreadsheet = new Spreadsheet(sampleFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @WebServlet(urlPatterns = "/*",  asyncSupported = true)
    @VaadinServletConfiguration(ui = ExcelUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }

}
