package com.qrsynergy.ui.view.viewdocument;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.DocumentType;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.model.helper.UserDocument;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.service.QRService;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.event.DashboardEvent;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.qrsynergy.ui.view.viewdocument.tabs.Details;
import com.qrsynergy.ui.view.viewdocument.tabs.EditCompanyRights;
import com.qrsynergy.ui.view.viewdocument.tabs.EditPeopleRights;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
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
        content.addComponents(ownLabel, buildQRItems(ownDocuments, RightType.OWNER));

        Label editLabel = new Label("Editable documents");
        content.addComponents(editLabel, buildQRItems(editDocuments, RightType.EDIT));

        Label viewLabel = new Label("Viewable documents");
        content.addComponents(viewLabel, buildQRItems(viewDocuments, RightType.VIEW));

        return content;
    }

    /**
     * Builds grids
     * Owned documents grid, editable documents grid or viewable documents grid
     * The parameter is set in buildContent method
     * @param userDocuments list of user documents
     * @return grid of the documents
     */
    private Component buildQRItems(List<UserDocument> userDocuments, RightType rightType){
        Grid<UserDocument> grid = new Grid<>();
        // grid.setSizeFull();
        GridScrollExtension<UserDocument> extension = new GridScrollExtension<>(grid);

        grid.setItems(userDocuments);

        // details
        grid.addComponentColumn(userDocument -> buildEditModal(userDocument, rightType))
                .setId("details")
                .setWidth(90)
                .setCaption("Details")
                .setResizable(false)
                .setSortable(false);

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
        grid.addColumn(userDocument -> openExcelInNewTab(userDocument, rightType) ,
                new ComponentRenderer()).setCaption( "View" )
                .setId("view")
                .setWidth(100)
                .setResizable(false)
                .setSortable(false);

        grid.setSizeFull();
        setGridHeight(userDocuments, grid);

        extension.setColumnResizeCompensationMode(ColumnResizeCompensationMode.RESIZE_GRID);
        extension.adjustGridWidth();

        return grid;
    }


    /**
     * Opens edit tab
     * Fetches QR from the UserDocument.
     * Opens a modal to edit information of QR
     * @param userDocument user document to fetch QR
     * @return modal for editing the QR
     */
    private Component buildEditModal(UserDocument userDocument, RightType rightType){
        Button openWindow = new Button();
        openWindow.setIcon(VaadinIcons.COG);
        openWindow.setHeight("25px");
        openWindow.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                QR qr = ((DashboardUI) UI.getCurrent()).qrService.findQRByUrl(userDocument.getUrl());
                VerticalLayout layout = new VerticalLayout();

                Window window = new Window();

                TabSheet tabSheet = new TabSheet();
                tabSheet.setSizeFull();
                layout.addComponent(tabSheet);
                // information tab
                Details details = new Details(qr);
                tabSheet.addTab(details.getContent(), "Info");

                // user can edit people's rights
                // i.e. add/remove user, change rights of users

                // Who is viewing it?
                User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());

                EditPeopleRights editPeopleRights = new EditPeopleRights(qr, rightType, user);
                tabSheet.addTab(editPeopleRights.getContent(), "People");

                EditCompanyRights editCompanyRights = new EditCompanyRights(qr, rightType);
                tabSheet.addTab(editCompanyRights.getContent(), "Company");

                // open modal
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


    /**
     * Helper method for setting the grid's height
     * max size is 10 rows
     * In case there is no rows, it sets to 1 row
     * @param userDocuments user documents
     * @param grid grid
     */
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

    /**
     * Builds thumbnail of the file
     * i.e. Excel or RAR etc.
     * @param userDocument user document
     * @return html version of the thumbnail
     */
    private String renderTypeThumbnail(UserDocument userDocument){
        if(userDocument.getDocumentType().equals(DocumentType.EXCEL)){
           return FontAwesome.FILE_EXCEL_O.getHtml();
        }
        else{
            // TODO
            return FontAwesome.GRADUATION_CAP.getHtml();
        }
    }

    /**
     * Builds a button that opens the excel in the new browser tab.
     * TODO
     * Excel should open in the current UI, not in a new UI
     * @param userDocument userDocument
     * @return button
     */
    private Button openExcelInNewTab(UserDocument userDocument, RightType rightType){
        Button btn = new Button();
        btn.setIcon(FontAwesome.EYE);
        btn.setHeight("25px");

        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                DashboardEventBus.post(new DashboardEvent.ExcelPageRequestedEvent(userDocument.getUrl()));
            }
        });
        return  btn;
    }
}
