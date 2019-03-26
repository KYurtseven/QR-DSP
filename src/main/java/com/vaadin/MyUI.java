package com.vaadin;

import java.io.*;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.servlet.annotation.WebServlet;

import com.vaadin.ui.*;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Theme("mytheme")
@Widgetset("com.vaadin.MyAppWidgetset")
public class MyUI extends UI implements Serializable{

    private Spreadsheet spreadsheet = null;
    private final VerticalLayout layout = new VerticalLayout();
    private XSSFWorkbook wb = null;
    private Button saveButton = null;
    private String TEMPLATE_PATH = "/home/egemen/Desktop/formbuiler/templ.xlsx";
    String path;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        layout.setSizeFull();
        setContent(layout);

        // Create a fileURI field and add to main view
        HorizontalLayout fileURIField = addFileURIField();
        layout.addComponent(fileURIField);
        layout.setExpandRatio(fileURIField, 0);

    }

    private HorizontalLayout addFileURIField() {
        HorizontalLayout fileURILayout = new HorizontalLayout();

        TextField excelURIField = new TextField();
        Button openButton = new Button("Open");
        Button createButton = new Button("Create");
        saveButton = new Button("Save");

        saveButton.addClickListener(click -> {
            if (spreadsheet != null) {
                try {
                    spreadsheet.write(path);
                    spreadsheet.read(new FileInputStream(path));
                } catch (FileNotFoundException fn) {
                    System.out.println("File not found");
                } catch (IOException ioe) {
                    System.out.println("IO exception");
                }
            }
        });

        // Add click listener to text field
        openButton.addClickListener(click -> {
            if (spreadsheet != null) {
                layout.removeComponent(spreadsheet);
            }
            path = excelURIField.getValue();
            initSpreadsheet(path);
            layout.addComponent(spreadsheet);
            layout.setExpandRatio(spreadsheet, 1);
        });

        createButton.addClickListener(click -> {
            if (spreadsheet != null) {
                layout.removeComponent(spreadsheet);
            }
            // Create a new file
            File newFile = new File("/home/egemen/Desktop/formbuiler/" + excelURIField.getValue() + ".xlsx");
            try {
                newFile.createNewFile();
                createSpreadSheet(newFile.toString());
                layout.addComponent(spreadsheet);
                layout.setExpandRatio(spreadsheet, 1);
            } catch (IOException io) {
                Notification.show("The file already exists!");
            }
        });

        fileURILayout.addComponent(excelURIField);
        fileURILayout.addComponent(openButton);
        fileURILayout.addComponent(saveButton);
        fileURILayout.addComponent(createButton);

        return fileURILayout;
    }

    private void initSpreadsheet(String pathname) {
        try {
            wb = new XSSFWorkbook(new FileInputStream(pathname));
            spreadsheet = new Spreadsheet();
            spreadsheet.setWorkbook(wb);
        } catch (IOException e) {
            Notification.show("File not found!!");
        }
    }

    private void createSpreadSheet (String pathname) {
        try {
            FileChannel src = new FileInputStream(TEMPLATE_PATH).getChannel();
            FileChannel dest = new FileOutputStream(pathname).getChannel();
            dest.transferFrom(src, 0, src.size());
        } catch (IOException io) {
            Notification.show("Something went wrong!!!");
        }

        initSpreadsheet(pathname);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
