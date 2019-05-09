package com.qrsynergy.ui.view.viewdocument;

import com.qrsynergy.controller.helper.UserDTO;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.DocumentType;
import com.qrsynergy.model.helper.UserDocument;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.service.QRService;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.ExcelUI;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.qrsynergy.ui.view.viewdocument.tabs.Details;
import com.qrsynergy.ui.view.viewdocument.tabs.EditPeople;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.extension.gridscroll.GridScrollExtension;
import org.vaadin.extension.gridscroll.shared.ColumnResizeCompensationMode;

import java.util.ArrayList;
import java.util.List;


public final class ViewDocumentView extends Panel implements  View{

    public static final String TITLE_ID = "viewdocument-title";
    private final VerticalLayout root;

    @Autowired
    QRService qrService;

    public ViewDocumentView(){
        addStyleName(ValoTheme.PANEL_BORDERLESS);

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
        root.addComponent(content);
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
     * accepts UserQR, fetches urls from it
     * @return list of urls
     */
    @Deprecated
    private List<String> userQRToUrl(List<UserDocument> userDocuments){
        List<String> urls = new ArrayList<>();
        for(int i = 0; i < userDocuments.size(); i++){
            urls.add(userDocuments.get(i).getUrl());
        }
        return urls;
    }

    /**
     * Creates 3 tables for owned, editable and viewable QRs.
     * @return 3 tables for UserDocuments
     */
    private Component buildContent(){
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());

        UserQR userQR = ((DashboardUI) UI.getCurrent()).userQRService.getUserQR(user);

        List<UserDocument> ownDocuments = userQR.getO_docs();
        List<UserDocument> editDocuments = userQR.getE_docs();
        List<UserDocument> viewDocuments = userQR.getV_docs();

        Label ownLabel = new Label("Owned documents");
        content.addComponents(ownLabel, buildQRItems(ownDocuments));

        Label editLabel = new Label("Editable documents");
        content.addComponents(editLabel, buildQRItems(editDocuments));

        Label viewLabel = new Label("Viewable documents");
        content.addComponents(viewLabel, buildQRItems(viewDocuments));

        return content;
    }

    /**
     * Builds grids
     * Owned documents grid, editable documents grid or viewable documents grid
     * The parameter is set in buildContent method
     * @param userDocuments list of user documents
     * @return grid of the documents
     */
    private Component buildQRItems(List<UserDocument> userDocuments){
        Grid<UserDocument> grid = new Grid<>();
        // grid.setSizeFull();
        GridScrollExtension<UserDocument> extension = new GridScrollExtension<>(grid);

        grid.setItems(userDocuments);

        // details
        grid.addComponentColumn(userDocument -> buildEditWindow(userDocument))
                .setId("details")
                .setWidth(90)
                .setCaption("Details")
                .setResizable(false)
                .setSortable(false);

        /*
        grid.addColumn(userDocument -> {
            Button btn = new Button();
            btn.setIcon(FontAwesome.EXPAND);
            btn.setHeight("25px");
            btn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    if(grid.isDetailsVisible(userDocument)){
                        grid.setDetailsVisible(userDocument, false);
                        setGridHeight(userDocuments, grid);
                    }
                    else{
                        grid.setDetailsVisible(userDocument, true);
                        grid.setHeightByRows(10);
                    }
                }
            });
            return btn;
        }, new ComponentRenderer()).setCaption("Details")
                .setId("details")
                .setWidth(90)
                .setResizable(false)
                .setSortable(false);
        */
        grid.addColumn(userDocument -> renderTypeThumbnail(userDocument), new HtmlRenderer())
                .setCaption("Type")
                .setId("type")
                .setWidth(100)
                .setResizable(false)
                .setSortable(true);

        grid.addColumn(UserDocument::getName).setCaption("Name")
                .setId("name")
                .setResizable(false)
                .setExpandRatio(1)
                .setSortable(true);

        // Button for opening excel in the new tab
        grid.addColumn(userDocument -> openExcelInNewTab(userDocument) ,
                new ComponentRenderer()).setCaption( "View" )
                .setId("view")
                .setWidth(100)
                .setResizable(false)
                .setSortable(false);


        /*
        grid.setDetailsGenerator(userDocument -> {
            QR qr = ((DashboardUI) UI.getCurrent()).qrService.findQRByUrl(userDocument.getUrl());
            VerticalLayout layout = new VerticalLayout();
            layout.setWidth(100, Unit.PERCENTAGE);

            Window window = new Window();


            TabSheet tabSheet = new TabSheet();
            tabSheet.setSizeFull();
            layout.addComponent(tabSheet);

            tabSheet.addTab(Details.qrInfoTab(qr), "Info");

            EditPeople editPeople = new EditPeople(qr);
            tabSheet.addTab(editPeople.getContent(), "Edit people");

            window.setContent(layout);

            return layout;
        });
    */
        grid.setSizeFull();

        setGridHeight(userDocuments, grid);


        extension.setColumnResizeCompensationMode(ColumnResizeCompensationMode.RESIZE_GRID);
        extension.adjustGridWidth();

        return grid;
    }


    private Component buildEditWindow(UserDocument userDocument){
        Button openWindow = new Button();
        openWindow.setIcon(VaadinIcons.COG);
        openWindow.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                QR qr = ((DashboardUI) UI.getCurrent()).qrService.findQRByUrl(userDocument.getUrl());
                VerticalLayout layout = new VerticalLayout();

                Window window = new Window();

                TabSheet tabSheet = new TabSheet();
                tabSheet.setSizeFull();
                layout.addComponent(tabSheet);

                tabSheet.addTab(Details.qrInfoTab(qr), "Info");

                EditPeople editPeople = new EditPeople(qr);
                tabSheet.addTab(editPeople.getContent(), "Edit people");

                window.setContent(layout);

                getUI().addWindow(window);


                layout.setWidth(1000, Unit.PIXELS);
                layout.setHeight(500, Unit.PIXELS);
                window.center();
                window.setModal(true);
            }
        });

        return openWindow;
    }


    private void setGridHeight(List<UserDocument> userDocuments, Grid grid){
        if(userDocuments.size() >= 10){
            grid.setHeightByRows(10);
        }
        else{
            if(userDocuments.size() == 0){
                grid.setHeightByRows(1);
                grid.appendFooterRow();
                grid.getFooterRow(0).getCell("name").setText("No entry is found");
            }
            else{
                grid.setHeightByRows(userDocuments.size());
            }
        }

    }

    private String renderTypeThumbnail(UserDocument userDocument){
        if(userDocument.getDocumentType().equals(DocumentType.EXCEL)){
           return FontAwesome.FILE_EXCEL_O.getHtml();
        }
        else{
            // TODO
            return FontAwesome.GRADUATION_CAP.getHtml();
        }
    }

    private Button openExcelInNewTab(UserDocument userDocument){
        Button btn = new Button();
        btn.setIcon(FontAwesome.EYE);
        btn.setHeight("25px");
        BrowserWindowOpener opener = new BrowserWindowOpener(ExcelUI.class);
        opener.extend(btn);
        opener.setParameter("qr_id", userDocument.getUrl());
        return  btn;
    }
}
