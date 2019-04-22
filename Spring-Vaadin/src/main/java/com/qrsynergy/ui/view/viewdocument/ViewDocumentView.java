package com.qrsynergy.ui.view.viewdocument;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.DocumentType;
import com.qrsynergy.model.helper.UserDocument;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.ExcelUI;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.client.ui.Icon;
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
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public final class ViewDocumentView extends Panel implements  View{

    public static final String TITLE_ID = "viewdocument-title";
    private final VerticalLayout root;
    private final String userRemovalConfirmation1 = "Are you sure to remove ";
    private final String userRemovalConfirmation3 = "from";
    private final String userRemovalConfirmation4_edit = " editing this QR";
    private final String userRemovalConfirmation4_view = " viewing this QR";



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
     * accepts UserQR, fetches urls from it
     * @return list of urls
     */
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

    private Component buildQRItems(List<UserDocument> userDocuments){
        Grid<UserDocument> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(userDocuments);

        grid.addColumn(userDocument -> renderTypeThumbnail(userDocument), new HtmlRenderer()).setCaption("Type");
        grid.addColumn(UserDocument::getName).setCaption("Name");
        // Button for opening excel in the new tab
        grid.addColumn(userDocument -> openExcelInNewTab(userDocument) ,
                new ComponentRenderer()).setCaption( "View" );
        grid.addColumn(userDocument -> {
            Button btn = new Button("Details");
            btn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    if(grid.isDetailsVisible(userDocument)){
                        grid.setDetailsVisible(userDocument, false);
                    }
                    else{
                        grid.setDetailsVisible(userDocument, true);
                    }
                }
            });
            return btn;
        }, new ComponentRenderer()).setCaption("Details");
        // details
        grid.setDetailsGenerator(userDocument -> {
            QR qr = ((DashboardUI) UI.getCurrent()).qrService.findQRByUrl(userDocument.getUrl());
            VerticalLayout layout = new VerticalLayout();
            TabSheet tabSheet = new TabSheet();
            layout.addComponent(tabSheet);
            tabSheet.addTab(qrInfoTab(qr), "Info");
            tabSheet.addTab(denemeTab(), "deneme");
            return layout;
        });

        return grid;
    }

    // TODO
    // DELETE
    private Component denemeTab(){
        VerticalLayout layout = new VerticalLayout();
        Label newLabel = new Label("deneme");
        layout.addComponent(newLabel);
        return layout;
    }

    /**
     * TODO
     * Better UI
     * @param qr QR
     * @return component to be rendered in the tab
     */
    private Component qrInfoTab(QR qr){
        VerticalLayout layout = new VerticalLayout();

        Label url = new Label("url: " + qr.getUrl());
        Label originalName = new Label("original name: " + qr.getOriginalName());
        Label isPublished = new Label("publish: " + qr.getPublished());
        Label documentType = new Label("document type: " + qr.getDocumentType());
        Label creationDate = new Label("creation date: " + qr.getCreationDate());
        Label expirationDate = new Label("expiration date: " + qr.getExpirationDate());

        layout.addComponents(url, originalName, isPublished, documentType, creationDate, expirationDate);
        return layout;
    }

    /**
     * In this tab, user can add or remore users.
     * Either for editing users or viewing users
     * TODO
     * Do it with grid.
     * @param qr QR
     * @param user_infos QR's user_info
     * @param type view or edit
     * @return
     */
    private Component editPeopleTab(QR qr, List<String> user_infos, String type){
        VerticalLayout layout = new VerticalLayout();
        for(String user_info: user_infos){
            HorizontalLayout hLayout = new HorizontalLayout();
            Label user_infoLabel = new Label(user_info);
            Button removeUserButton = new Button("-");
            removeUserButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    ConfirmDialog.show(UI.getCurrent(), buildConfirmationUserRemoval(user_info, type),
                            new ConfirmDialog.Listener() {
                                @Override
                                public void onClose(ConfirmDialog confirmDialog) {
                                    if(confirmDialog.isConfirmed()){
                                        // TODO
                                    }
                                    else{
                                        // TODO
                                    }
                                }
                            });
                }
            });
        }
        return null;
    }

    /**
     * TODO
     * Better string building
     * @param user_info to be removed user's email
     * @param type view or edit
     * @return string to be rendered
     */
    private String buildConfirmationUserRemoval(String user_info, String type){
        if(type.equals("view")){
            return userRemovalConfirmation1 + user_info + userRemovalConfirmation3 + userRemovalConfirmation4_view;
        }
        else{
            return userRemovalConfirmation1 + user_info + userRemovalConfirmation3 +   userRemovalConfirmation4_edit;
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
        Button btn = new Button("Open");
        BrowserWindowOpener opener = new BrowserWindowOpener(ExcelUI.class);
        opener.extend(btn);
        opener.setParameter("qr_id", userDocument.getUrl());
        return  btn;
    }
}
