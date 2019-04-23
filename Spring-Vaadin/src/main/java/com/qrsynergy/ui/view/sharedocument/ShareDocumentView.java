package com.qrsynergy.ui.view.sharedocument;

import com.qrsynergy.model.Company;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.DocumentType;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public final class ShareDocumentView extends Panel implements View{

    public static final String TITLE_ID = "sharedocument-title";
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

    private List<Company> companyListDB = new ArrayList<>();
    private List<String> companyListUI = new ArrayList<>();

    private AdditionalOptionsInfo additionalOptionsInfo = new AdditionalOptionsInfo();

    /**
     * Constructor
     */
    public ShareDocumentView(){
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

    /**
     * Adds title
     * TODO
     * Every Page will implement this
     * Maybe write it as a Interface???
     * @return header of the page
     */
    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        Label titleLabel = new Label("Share New Document");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    /**
     * Adds main content, wizard
     * @return
     */
    private Component buildContent() {
        Wizard wizard = new Wizard();

        wizard.setSizeFull();
        addWizardListeners(wizard);
        wizard.setUriFragmentEnabled(true);

        // add pages
        wizard.addStep(new UploadFileStep(firstStepInfo), "Upload The Document");
        wizard.addStep(new AddPeopleStep("view", selectViewEmails, emailViewDataProvider, wizard), "Individual view rights");
        wizard.addStep(new AddPeopleStep("edit", selectEditEmails, emailEditDataProvider, wizard), "Individual edit rights");

        // TODO
        // Cast company list to the string
        // in better for loop
        companyListDB = ((DashboardUI) UI.getCurrent()).companyService.findAll();
        for (Company company: companyListDB) {
            companyListUI.add(company.getName());
        }
        selectViewCompanies.setItems(companyListUI);
        selectViewCompanies.setRows(companyRowCount);

        selectEditCompanies.setItems(companyListUI);
        selectEditCompanies.setRows(companyRowCount);

        wizard.addStep(new AddCompanyStep("view", selectViewCompanies, wizard), "Company view rights");
        wizard.addStep(new AddCompanyStep("edit", selectEditCompanies, wizard), "Company edit rights");

        wizard.addStep(new AdditionalOptionsStep(additionalOptionsInfo), "Additional options");

        return wizard;
    }

    /**
     * Listens for wizard events
     *
     * When the wizard is finished, saves the data to database and excel to disk
     *
     * TODO
     * When cancelled deletes/nullifies objects
     *
     * @param wizard
     */
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
        try{
            if(firstStepInfo != null){
                if(firstStepInfo.getUrl() != null){

                    firstStepInfo.writeByteToFile();
                    // File is written to the disk

                    // Convert file to csv, and save that too
                    if(firstStepInfo.getDocumentType().equals(DocumentType.EXCEL)){
                        firstStepInfo.writeByteToCSV();
                    }

                    QR qr = prepareQRFields();

                    // save document to the database
                    ((DashboardUI) UI.getCurrent()).qrService.saveNewDocument(qr);
                }
            }
        }
        catch(Exception e){
            Notification fileUploadExceptionNotification = new Notification("Unknown error occured");
            fileUploadExceptionNotification.setDelayMsec(2000);
            fileUploadExceptionNotification.setPosition(Position.MIDDLE_CENTER);
            fileUploadExceptionNotification.show(Page.getCurrent());
            System.out.println("File upload exception: " + e);
        }
    }

    /**
     * Prepare QR's fields for saving
     * @return
     */
    private QR prepareQRFields(){
        List<String> viewEmails = checkEmails(emailViewDataProvider);
        List<String> editEmails = checkEmails(emailEditDataProvider);

        List<String> viewCompanies = checkCompanies(selectViewCompanies);
        List<String> editCompanies = checkCompanies(selectEditCompanies);

        QR qr = new QR(firstStepInfo);

        User user = (User) VaadinSession.getCurrent()
                .getAttribute(User.class.getName());
        // set owner info
        qr.setO_info(user.getEmail());

        // set people info
        qr.setV_info(viewEmails);
        qr.setE_info(editEmails);

        // set company info
        qr.setV_company(viewCompanies);
        qr.setE_company(editCompanies);

        // set additional info
        qr.setPublic(additionalOptionsInfo.isPublic());
        qr.setPublished(additionalOptionsInfo.isPublished());
        qr.setExpirationDate(additionalOptionsInfo.getExpirationDate());

        return qr;
    }

    /**
     * Filters email that user has provided
     * @param dataProvider
     * @return correct list of emails
     */
    public List<String> checkEmails(ListDataProvider<String> dataProvider){

        List<String> filteredEmails = new ArrayList<>();

        for(final String email: dataProvider.getItems()){
            boolean valid = EmailValidator.getInstance().isValid(email);
            if(valid){
                filteredEmails.add(email);
            }
        }
        return filteredEmails;
    }

    /**
     * Check selected companies with the companies
     * that have already present in the scope in case
     * the user somehow changed the values
     */
    public List<String> checkCompanies(TwinColSelect<String> select){
        List<String> list = new ArrayList<>();

       List<String> selectedList = new ArrayList<>(select.getSelectedItems());

       // Check for user inputs
        for (String companyName: selectedList) {
            for(int i = 0; i < companyListDB.size(); i++){
                if(companyName.equals(companyListDB.get(i).getName())){
                    list.add(companyListDB.get(i).getEmailExtension());
                    break;
                }
            }
        }

       return list;
    }

    /**
     * TODO
     * We don't need to delete the file on disk since it was
     * never ever saved.
     * Nullify objects
     *
     * @param event
     */
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
