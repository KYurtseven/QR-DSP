package com.qrsynergy.ui.view.viewdocument;

import com.qrsynergy.model.User;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.qrsynergy.ui.view.createdocument.UploadFileStep;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;
import java.io.IOException;

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
        User user = (User) VaadinSession.getCurrent()
                .getAttribute(User.class.getName());

        String testExcelText = "e3e479e8-c6e8-47ec-ae7f-faefa34f1e50";
        Button testExcel = new Button(testExcelText);

        testExcel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final String path = UploadFileStep.uploadLocation + testExcelText + ".xlsx";

                File excel = new File(path);
                try{
                    Spreadsheet spreadsheet = new Spreadsheet(excel);
                    spreadsheet.setReportStyle(true);
                    spreadsheet.setActiveSheetProtected("asdasdasd");
                    root.addComponent(spreadsheet);
                    root.setExpandRatio(spreadsheet,1);

                }catch(IOException e){
                    System.out.println("io exception: " + e);
                }
            }
        });
        return testExcel;
    }
}
