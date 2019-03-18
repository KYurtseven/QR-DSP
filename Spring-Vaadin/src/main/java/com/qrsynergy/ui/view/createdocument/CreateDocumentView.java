package com.qrsynergy.ui.view.createdocument;

import com.google.common.eventbus.Subscribe;
import com.qrsynergy.ui.event.DashboardEvent;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.io.FileUtils;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public final class CreateDocumentView extends Panel implements View{

    public static final String TITLE_ID = "createdocument-title";

    private final VerticalLayout root;
    private WizardCancelledEvent cancelledEvent;
    private WizardCompletedEvent completedEvent;

    // Upload file page
    private FirstStepInfo firstStepInfo = new FirstStepInfo();

    // View Emails page
    private NativeSelect<String> selectViewEmails = new NativeSelect<>("View List");
    private ListDataProvider<String> viewDataProvider = DataProvider.ofCollection(new ArrayList<>());

    // Edit emails page
    private NativeSelect<String> selectEditEmails = new NativeSelect<>("Edit List");
    private ListDataProvider<String> editDataProvider = DataProvider.ofCollection(new ArrayList<>());

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

        Label titleLabel = new Label("Generate New QR");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    private Component buildContent() {
        Wizard wizard = new Wizard();

        wizard.setSizeFull();
        addWizardListeners(wizard);
        wizard.setUriFragmentEnabled(true);

        // add pages
        wizard.addStep(new UploadFileStep(firstStepInfo), "Upload The Document");
        wizard.addStep(new AddPeopleStep("view", selectViewEmails, viewDataProvider, wizard), "View rights");
        wizard.addStep(new AddPeopleStep("edit", selectEditEmails, editDataProvider, wizard), "Edit rights");

        return wizard;
    }

    public void addWizardListeners(Wizard wizard){
        completedEvent = new WizardCompletedEvent(wizard);
        cancelledEvent = new WizardCancelledEvent(wizard);

        wizard.getFinishButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                wizardCompleted(completedEvent);
            }
        });
        wizard.getCancelButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                wizardCancelled(cancelledEvent);
            }
        });
    }

    public void wizardCompleted(WizardCompletedEvent event) {
        System.out.println("Completed");
        try{
            if(firstStepInfo != null){
                if(firstStepInfo.getUrl() != null){
                    // There is the file

                    File targetFile = new File(UploadFileStep.uploadLocation + firstStepInfo.getDiskName());

                    FileUtils.writeByteArrayToFile(
                            targetFile,
                            firstStepInfo.getFileInBytes()
                    );
                    // File is written to the disk
                    List<String> viewEmails = checkEmails(viewDataProvider);

                }
            }
        }
        catch(IOException e){
            Notification fileUploadExceptionNotification = new Notification("Unknown error occured");
            fileUploadExceptionNotification.setDelayMsec(2000);
            fileUploadExceptionNotification.setPosition(Position.MIDDLE_CENTER);
            fileUploadExceptionNotification.show(Page.getCurrent());
            System.out.println("File upload exception: " + e);
        }
    }

    public List<String> checkEmails(ListDataProvider<String> dataProvider){
        List<String> filteredEmails = new ArrayList<>();
        for(final String email: dataProvider.getItems()){

            System.out.println("email: " + email);
        }
        return filteredEmails;
    }

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
