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
import java.util.Set;

@SuppressWarnings("serial")
public final class CreateDocumentView extends Panel implements View{

    public static final String TITLE_ID = "createdocument-title";
    public static final Integer companyRowCount = 10;

    private final VerticalLayout root;
    private WizardCancelledEvent cancelledEvent;
    private WizardCompletedEvent completedEvent;

    // Upload file page
    private FirstStepInfo firstStepInfo = new FirstStepInfo();

    // View Emails page
    private NativeSelect<String> selectViewEmails = new NativeSelect<>("View List");
    private ListDataProvider<String> emailViewDataProvider = DataProvider.ofCollection(new ArrayList<>());

    // Edit emails page
    private NativeSelect<String> selectEditEmails = new NativeSelect<>("Edit List");
    private ListDataProvider<String> emailEditDataProvider = DataProvider.ofCollection(new ArrayList<>());

    // View company page
    private TwinColSelect<String> selectViewCompanies = new TwinColSelect<>("Company List");

    // Edit company page
    private TwinColSelect<String> selectEditCompanies = new TwinColSelect<>("Company List");

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
        wizard.addStep(new AddPeopleStep("view", selectViewEmails, emailViewDataProvider, wizard), "Individual view rights");
        wizard.addStep(new AddPeopleStep("edit", selectEditEmails, emailEditDataProvider, wizard), "Individual edit rights");

        // Fetch company list from the database
        // TODO
        // This is mock data
        selectViewCompanies.setItems("Ford", "Nissan", "Opel");
        selectViewCompanies.setRows(companyRowCount);

        selectEditCompanies.setItems("Ford", "Nissan", "Opel");
        selectEditCompanies.setRows(companyRowCount);

        wizard.addStep(new AddCompanyStep("view", selectViewCompanies, wizard), "Company view rights");
        wizard.addStep(new AddCompanyStep("edit", selectEditCompanies, wizard), "Company edit rights");

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

                    // TODO
                    List<String> viewEmails = checkEmails(emailViewDataProvider);
                    List<String> editEmails = checkEmails(emailEditDataProvider);

                    List<String> viewCompanies = checkCompanies(selectViewCompanies);
                    List<String> editCompanies = checkCompanies(selectEditCompanies);

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

    // TODO
    // Filter invalid emails
    public List<String> checkEmails(ListDataProvider<String> dataProvider){
        List<String> filteredEmails = new ArrayList<>();
        for(final String email: dataProvider.getItems()){

            System.out.println("email: " + email);
        }
        return filteredEmails;
    }

    // TODO
    // Check them in the database
    public List<String> checkCompanies(TwinColSelect<String> select){
        List<String> list = new ArrayList<>(select.getValue());

        return list;
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
