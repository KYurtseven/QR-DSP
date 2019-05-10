package com.qrsynergy.ui.view;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.event.DashboardEvent;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.qrsynergy.ui.view.sharedocument.steps.UploadAndAddPeople;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;

import java.io.File;
import java.io.IOException;

// TODO
// Is this necessary?
@Theme("mytheme")
public class ExcelView extends HorizontalLayout {

    private String url;
    private User user;
    private QR qr;
    private Spreadsheet spreadsheet;

    public ExcelView(String url, User user){
        setSizeFull();
        setMargin(false);
        setSpacing(false);

        DashboardEventBus.register(this);
        this.url = url;
        this.user = user;
        // fetch qr from the database
        this.qr = ((DashboardUI) UI.getCurrent()).qrService.findQRByUrl(url);

        addComponent(buildFields());

    }

    private Component buildFields(){
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setHeight("50px");
        layout.addComponents(returnToMainView(),saveExcelButton());

        //initialize spreadsheet
        initSpreadsheet();
        content.addComponents(layout, spreadsheet);


        return content;
    }


    private void initSpreadsheet() {
        File sampleFile = new File(UploadAndAddPeople.uploadLocation + qr.getUrl() + ".xlsx");
        try {
            spreadsheet = new Spreadsheet(sampleFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return button that saves excel to the disk
     */
    private Component saveExcelButton(){
        Button saveButton = new Button();
        saveButton.setIcon(VaadinIcons.CLOUD_UPLOAD);
        // TODO
        return saveButton;
    }

    /**
     * Builds a button which returns user to the Main View
     * @return button for returning to the main view
     */
    private Component returnToMainView(){
        Button button = new Button();
        button.setIcon(VaadinIcons.BACKSPACE);

        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                DashboardEventBus.post(new DashboardEvent.ExcelPreviousPageEvent());
            }
        });

        return button;
    }
}
