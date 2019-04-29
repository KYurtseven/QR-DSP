package com.qrsynergy.ui.view.viewdocument;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import java.awt.*;
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

            tabSheet.addTab(Details.qrInfoTab(qr), "Info");

            EditPeople editPeople = new EditPeople(qr);
            tabSheet.addTab(editPeople.getContent(), "Edit people");

            return layout;
        });

        return grid;
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
