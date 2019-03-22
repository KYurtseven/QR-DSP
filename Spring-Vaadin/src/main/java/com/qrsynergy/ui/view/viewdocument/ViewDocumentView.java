package com.qrsynergy.ui.view.viewdocument;

import com.qrsynergy.model.User;
import com.qrsynergy.model.UserDocument;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.ui.DashboardUI;
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
import java.util.List;

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
            Label qrName = new Label(userDocument.getName());

            Button openDocument = new Button("Open");
            openDocument.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    final String path = UploadFileStep.uploadLocation + userDocument.getUrl() + ".xlsx";

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

            column.addComponents(qrName, openDocument);
            row.addComponent(column);
        }

        return row;
    }

}
