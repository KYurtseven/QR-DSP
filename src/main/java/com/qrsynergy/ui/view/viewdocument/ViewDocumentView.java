package com.qrsynergy.ui.view.viewdocument;

import com.qrsynergy.GlobalSettings;
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
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.extension.gridscroll.GridScrollExtension;
import org.vaadin.extension.gridscroll.shared.ColumnResizeCompensationMode;
import org.vaadin.hene.popupbutton.PopupButton;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public final class ViewDocumentView extends Panel implements  View{

    public static final String TITLE_ID = "viewdocument-title";
    private final VerticalLayout root;
    private List<UserDocument> ownDocuments;
    private List<UserDocument> editDocuments;
    private List<UserDocument> viewDocuments;

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

        ownDocuments = userQR.getO_docs();
        editDocuments = userQR.getE_docs();
        viewDocuments = userQR.getV_docs();

        Label ownLabel = new Label("Owned documents");
        content.addComponents(ownLabel, buildQRItems(ownDocuments, RightType.OWNER));

        Label editLabel = new Label("Editable documents");
        content.addComponents(editLabel, buildQRItems(editDocuments, RightType.EDIT));

        Label viewLabel = new Label("Viewable documents");
        content.addComponents(viewLabel, buildQRItems(viewDocuments, RightType.VIEW));

        return content;
    }


    private Component buildDetailsPopup(UserDocument userDocument, RightType rightType, Grid grid){
        Button actionButton = new Button("Action");
        actionButton.setId("action-button");
        actionButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        actionButton.setStyleName("action-button");

        actionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                grid.deselectAll();
                grid.select(userDocument);

                Window window = new Window();

                VerticalLayout popupContent = new VerticalLayout();
                // more information
                popupContent.addComponent(buildEditModal(userDocument, rightType, window));

                // view
                if(userDocument.getDocumentType().equals(DocumentType.EXCEL))
                    popupContent.addComponent(openExcelInNewTab(userDocument, rightType, window));
                else if(userDocument.getDocumentType().equals(DocumentType.PDF))
                    popupContent.addComponent(openPdfInNewTab(userDocument));

                //download document
                popupContent.addComponent(downloadDocument(userDocument, window));
                // Add delete
                if(rightType.equals(RightType.OWNER))
                    popupContent.addComponent(deleteDocument(userDocument, window,grid));


                // open modal
                window.setContent(popupContent);
                window.setResizable(false);

                getUI().addWindow(window);
                popupContent.setWidth(125, Unit.PIXELS);
                popupContent.setHeight(150, Unit.PIXELS);
                window.center();
                window.setModal(true);
            }
        });


        return actionButton;
    }

    private Component openPdfInNewTab(UserDocument userDocument) {

        Button btn = new Button("View");
        btn.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window window2 = new Window();
                window2.setWidth("100%");
                window2.setHeight("100%");
                BrowserFrame e = new BrowserFrame("PDF File", new FileResource(new File(GlobalSettings.getUploadLocation() + userDocument.getUrl() + ".pdf")));
                e.setWidth("100%");
                e.setHeight("100%");
                window2.setContent(e);
                window2.center();
                window2.setModal(true);
                window2.setResizable(false);
                UI.getCurrent().addWindow(window2);
            }
        });
        return btn;
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
        grid.addComponentColumn(userDocument -> buildDetailsPopup(userDocument, rightType, grid))
                .setId("details")
                .setWidth(90)
                .setCaption("Details")
                .setResizable(false)
                .setSortable(false);

        /*
        grid.addComponentColumn(userDocument -> buildEditModal(userDocument, rightType))
                .setId("details")
                .setWidth(90)
                .setCaption("Details")
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

        /*
        // Button for opening excel in the new tab
        grid.addColumn(userDocument -> openExcelInNewTab(userDocument, rightType) ,
                new ComponentRenderer()).setCaption( "View" )
                .setId("view")
                .setWidth(100)
                .setResizable(false)
                .setSortable(false);
        */
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
    private Button buildEditModal(UserDocument userDocument, RightType rightType, Window window){
        Button openWindow = new Button("More");
        openWindow.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        //openWindow.setIcon(VaadinIcons.COG);
        //openWindow.setHeight("25px");

        openWindow.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window.close();
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
        else if(userDocument.getDocumentType().equals(DocumentType.ZIP)){
            return FontAwesome.FILE_ZIP_O.getHtml();
        }
        else{
            return FontAwesome.FILE_PDF_O.getHtml();
        }
    }

    /**
     * Builds a button that opens the excel in the new browser tab.
     * TODO
     * Excel should open in the current UI, not in a new UI
     * @param userDocument userDocument
     * @return button
     */
    private Button openExcelInNewTab(UserDocument userDocument, RightType rightType, Window window){
        Button btn = new Button("View");

        btn.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        // btn.setIcon(FontAwesome.EYE);
        // btn.setHeight("25px");

        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window.close();
                DashboardEventBus.post(new DashboardEvent.ExcelPageRequestedEvent(userDocument.getUrl()));
            }
        });
        return  btn;
    }

    /**
     *
     * @param userDocument userDocument
     * @param window window
     * @return button
     */
    private Button deleteDocument(UserDocument userDocument, Window window, Grid grid) {
        Button btn = new Button("Delete");

        btn.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED);


        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window deleteConfirmationWindow = new Window();
                Label deletionLabel = new Label("Are you sure you want to delete this document?");
                VerticalLayout windowLayout = new VerticalLayout();
                HorizontalLayout cancelAndDeleteButtons = new HorizontalLayout();
                //cancel button
                cancelAndDeleteButtons.addComponent(cancelButton(deleteConfirmationWindow));
                //delete confirmation button
                cancelAndDeleteButtons.addComponent(deleteConfirmationButton(deleteConfirmationWindow,window,userDocument,grid));

                windowLayout.addComponents(deletionLabel,cancelAndDeleteButtons);
                deleteConfirmationWindow.setContent(windowLayout);
                getUI().addWindow(deleteConfirmationWindow);

                deleteConfirmationWindow.setWidth(450, Unit.PIXELS);
                deleteConfirmationWindow.setHeight(100, Unit.PIXELS);
                deleteConfirmationWindow.center();
                deleteConfirmationWindow.setModal(true);
            }
        });
        return btn;
    }

    private  Button cancelButton(Window deleteConfirmationWindow) {
        Button btn = new Button("Cancel");

        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                deleteConfirmationWindow.close();
            }
        });
        return btn;
    }

    private  Button deleteConfirmationButton(Window deleteConfirmationWindow, Window mainWindow, UserDocument userDocument, Grid grid) {
        Button btn = new Button("Delete");
        btn.addStyleName(ValoTheme.BUTTON_DANGER);
        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                deleteConfirmationWindow.close();
                mainWindow.close();

                QR qr = ((DashboardUI) UI.getCurrent()).qrService.findQRByUrl(userDocument.getUrl());
                deleteFromOwner(qr,userDocument);
                deleteFromUserEdit(qr,userDocument);
                deleteFromUserView(qr,userDocument);
                ((DashboardUI) UI.getCurrent()).qrService.removeQR(userDocument.getUrl());

                if(userDocument.getDocumentType().equals(DocumentType.EXCEL))
                {
                    File excelFile = new File(GlobalSettings.getUploadLocation() + userDocument.getUrl() + ".xlsx");
                    File csvFile = new File(GlobalSettings.getUploadLocation() + userDocument.getUrl() + ".csv");
                    File tempFile = new File(GlobalSettings.getTempLocation() + userDocument.getUrl() + "\\"  + userDocument.getName());
                    excelFile.delete();
                    csvFile.delete();
                    tempFile.delete();
                }
                else if(userDocument.getDocumentType().equals(DocumentType.PDF)){
                    File pdfFile = new File(GlobalSettings.getUploadLocation() + userDocument.getUrl() + ".pdf");
                    File tempFile = new File(GlobalSettings.getTempLocation() + userDocument.getUrl() + "\\"  + userDocument.getName());
                    pdfFile.delete();
                    tempFile.delete();
                }
                else if(userDocument.getDocumentType().equals(DocumentType.ZIP)){
                    File zipFile = new File(GlobalSettings.getUploadLocation() + userDocument.getUrl() + ".zip");
                    File tempFile = new File(GlobalSettings.getTempLocation() + userDocument.getUrl() + "\\"  + userDocument.getName());
                    zipFile.delete();
                    tempFile.delete();
                }

                for (UserDocument u:
                        ownDocuments) {
                    if(u.getUrl().equals(userDocument.getUrl())){
                        ownDocuments.remove(u);
                        break;
                    }
                }
                grid.getDataProvider().refreshAll();
            }
        });
        return btn;
    }

    private void deleteFromOwner(QR qr, UserDocument userDocument) {
        UserQR ownerQr = ((DashboardUI) UI.getCurrent()).userQRService.getUserQrByEmail(qr.getO_info());
        for (UserDocument u: ownerQr.getO_docs()) {
            if(u.getUrl().equals(userDocument.getUrl()))
            {
                ownerQr.getO_docs().remove(u);
                break;
            }
        }
        ((DashboardUI) UI.getCurrent()).userQRService.saveUserQR(ownerQr);
    }

    private void deleteFromUserEdit(QR qr, UserDocument userDocument){
        List<String> editEmailList = qr.getE_info();

        for(String email: editEmailList)
        {
            UserQR userQR = ((DashboardUI) UI.getCurrent()).userQRService.getUserQrByEmail(email);
            for( UserDocument u: userQR.getE_docs()){
                if(u.getUrl().equals(userDocument.getUrl())){
                    userQR.getE_docs().remove(u);
                    break;
                }
            }
            ((DashboardUI) UI.getCurrent()).userQRService.saveUserQR(userQR);
        }
    }

    private void deleteFromUserView(QR qr, UserDocument userDocument){
        List<String> viewEmailList = qr.getV_info();

        for(String email: viewEmailList)
        {
            UserQR userQR = ((DashboardUI) UI.getCurrent()).userQRService.getUserQrByEmail(email);
            for( UserDocument u: userQR.getV_docs()){
                if(u.getUrl().equals(userDocument.getUrl())){
                    userQR.getV_docs().remove(u);
                    break;
                }
            }
            ((DashboardUI) UI.getCurrent()).userQRService.saveUserQR(userQR);
        }
    }

    private Button downloadDocument(UserDocument userDocument, Window window){
        Button btn = new Button("Download");
        btn.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);


        Resource res = null;
        File originalFile = null;
        File tempFile = null;

        try{
            if(userDocument.getDocumentType().equals(DocumentType.EXCEL)) {
                originalFile = new File(GlobalSettings.getUploadLocation() + userDocument.getUrl() + ".xlsx");
            }
            else if(userDocument.getDocumentType().equals(DocumentType.PDF)) {
                originalFile = new File(GlobalSettings.getUploadLocation() + userDocument.getUrl() + ".pdf");
            }
            else if(userDocument.getDocumentType().equals(DocumentType.ZIP)){
                originalFile = new File(GlobalSettings.getUploadLocation() + userDocument.getUrl() + ".zip");
            }
            tempFile = new File(GlobalSettings.getTempLocation() + userDocument.getUrl() + "\\"  + userDocument.getName());
            FileUtils.copyFile(originalFile, tempFile);
            res = new FileResource(tempFile);
        }catch (Exception e) {

        }
        FileDownloader fd = new FileDownloader(res);
        fd.extend(btn);


        return btn;
    }
}
