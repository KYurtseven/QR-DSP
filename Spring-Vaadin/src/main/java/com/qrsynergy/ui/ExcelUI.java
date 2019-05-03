package com.qrsynergy.ui;

import java.io.*;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import com.qrsynergy.ui.view.sharedocument.steps.UploadAndAddPeople;
import com.vaadin.ui.*;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.SliderPanelStyles;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;




@Theme("mytheme")
public class ExcelUI extends UI{

    private Spreadsheet spreadsheet = null;
    private String qr_id;
    private SliderPanel rightSlider = null;
    private Button buttonInSlider = new Button("Comment");
    private TextArea textArea = new TextArea("hi");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        setContent(horizontalLayout);

        qr_id = vaadinRequest.getParameter( "qr_id" );

        //initialize spreadsheet
        initSpreadsheet();
        horizontalLayout.addComponent(spreadsheet);
        //layout.addComponent(spreadsheet);
        horizontalLayout.setSizeFull();
        horizontalLayout.setExpandRatio(spreadsheet, (float)1);

        //initialize Slider
        initSliderPanel();
        horizontalLayout.addComponent(rightSlider);
        horizontalLayout.setExpandRatio(rightSlider,(float) 0);
    }

    private void initSpreadsheet() {
        File sampleFile = new File(UploadAndAddPeople.uploadLocation + qr_id + ".xlsx");
        try {
            spreadsheet = new Spreadsheet(sampleFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSliderPanel()
    {
        VerticalLayout sliderVerticalLayout = new VerticalLayout();
        sliderVerticalLayout.addComponent(this.textArea);

        rightSlider = new SliderPanelBuilder(sliderVerticalLayout)
                                .expanded(false)
                                .mode(SliderMode.RIGHT)
                                .caption("Slider")
                                .tabPosition(SliderTabPosition.MIDDLE)
                                .autoCollapseSlider(true)
                                .flowInContent(true)
                                .style(SliderPanelStyles.ICON_BLACK)
                                //.zIndex(9980)
                                .build();

    }

    @WebServlet(urlPatterns = "/*",  asyncSupported = true)
    @VaadinServletConfiguration(ui = ExcelUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }

}
