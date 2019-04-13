package com.qrsynergy.ui.view.viewdocument;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.UserDocument;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.ExcelUI;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;


public final class ViewDocumentView extends Panel implements  View{

    public static final String TITLE_ID = "viewdocument-title";
    private final VerticalLayout root;

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

        List<String> ownUrls = userQRToUrl(userQR.getO_docs());
        List<String> editUrls = userQRToUrl(userQR.getE_docs());
        List<String> viewUrls = userQRToUrl(userQR.getV_docs());

        List<QR> ownQRs = ((DashboardUI) UI.getCurrent()).qrService.findQRListByUrls(ownUrls);
        List<QR> editQRs = ((DashboardUI) UI.getCurrent()).qrService.findQRListByUrls(editUrls);
        List<QR> viewQRs = ((DashboardUI) UI.getCurrent()).qrService.findQRListByUrls(viewUrls);

        Label ownLabel = new Label("Owned documents");
        content.addComponents(ownLabel, buildQRItems(ownQRs));

        Label editLabel = new Label("Editable documents");
        content.addComponents(editLabel, buildQRItems(editQRs));

        Label viewLabel = new Label("Viewable documents");
        content.addComponents(viewLabel, buildQRItems(viewQRs));

        return content;
    }

    private Component buildQRItems(List<QR> QRs){
        Grid<QR> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(QRs);

        grid.addColumn(QR::getOriginalName).setCaption("Name");
        grid.addColumn(QR::getPublic).setCaption("Is Public");
        grid.addColumn(QR::getCreationDate).setCaption("Creation Date");
        grid.addColumn(QR::getExpirationDate).setCaption("Expiration Date");
        // Button for opening excel in the new tab
        grid.addColumn(qr -> openExcelInNewTab(qr) ,
                new ComponentRenderer()).setCaption( "View" );
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        return grid;
    }

    private Button openExcelInNewTab(QR qr){
        Button btn = new Button("Open");
        BrowserWindowOpener opener = new BrowserWindowOpener(ExcelUI.class);
        opener.extend(btn);
        opener.setParameter("qr_id", qr.getUrl());
        return  btn;
    }
}
