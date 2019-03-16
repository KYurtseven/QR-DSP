package com.qrsynergy.ui.view.createdocument;

import com.qrsynergy.model.QR;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public final class CreateDocumentView extends Panel implements View{


    public static final String TITLE_ID = "dashboard-title";

    private Wizard wizard;

    private Label titleLabel;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;

    private FirstStepInfo firstStepInfo = new FirstStepInfo();
    private List<String> viewEmails  = new ArrayList<>();
    private List<String> editEmails  = new ArrayList<>();

    public CreateDocumentView(){
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
        root.addComponent(content);
        root.setExpandRatio(content, 1);

    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel = new Label("Generate New QR");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        // TODO
        // add contents here!
        wizard = new Wizard();

        wizard.setUriFragmentEnabled(true);
        wizard.addStep(new UploadFileStep(firstStepInfo), "Upload The Document");
        wizard.addStep(new AddPeopleStep("view", viewEmails), "View rights");
        wizard.addStep(new AddPeopleStep("edit", editEmails), "Edit rights");

        //System.out.println("QR in CreateDocumentView: " + qr);
        dashboardPanels.addComponent(wizard);
        return dashboardPanels;
    }

    public void wizardCompleted(WizardCompletedEvent event) {
        System.out.println("Completed");
    }

    // TODO
    // Overwrite wizardCancelled method to call this method
    public void wizardCancelled(WizardCancelledEvent event) {
        try{
            if(firstStepInfo != null){
                if(firstStepInfo.getUrl() != null){
                    // delete the file on the disk
                    File toBeDeletedFile = new File(UploadFileStep.uploadLocation + firstStepInfo.getDiskName());
                    toBeDeletedFile.delete();
                }
            }
            Notification.show("Wizard is cancelled", Notification.Type.ASSISTIVE_NOTIFICATION);

        }catch(Exception e){
            Notification.show("Not handled step cancel error", Notification.Type.ASSISTIVE_NOTIFICATION);
            System.out.println("Wizard cancelled error: " + e);
        }

    }

}
