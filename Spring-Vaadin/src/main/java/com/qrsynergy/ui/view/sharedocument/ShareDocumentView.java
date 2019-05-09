package com.qrsynergy.ui.view.sharedocument;

import com.qrsynergy.model.Company;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.qrsynergy.ui.view.sharedocument.infos.AdditionalOptionsInfo;
import com.qrsynergy.ui.view.sharedocument.infos.CompanyInfo;
import com.qrsynergy.ui.view.sharedocument.infos.FileInfo;
import com.qrsynergy.ui.view.sharedocument.infos.PeopleInfo;
import com.qrsynergy.ui.view.sharedocument.steps.AddCompanyStep;
import com.qrsynergy.ui.view.sharedocument.steps.AdditionalOptionsStep;
import com.qrsynergy.ui.view.sharedocument.steps.UploadAndAddPeople;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.validator.routines.EmailValidator;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import com.qrsynergy.ui.view.helper.qrgenerator.QRGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public final class ShareDocumentView extends Panel implements View{

    public static final String TITLE_ID = "sharedocument-title";

    private final VerticalLayout root;
    private WizardCancelledEvent cancelledEvent;
    private WizardCompletedEvent completedEvent;

    // UploadFile and AddPeople page
    private FileInfo fileInfo = new FileInfo();
    private List<PeopleInfo> peopleInfoList = new ArrayList<>();

    // Company page
    private List<Company> companyListDB;
    private List<CompanyInfo> companyInfoList = new ArrayList<>();

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
        // Upload and add people step
        wizard.addStep(new UploadAndAddPeople(fileInfo, peopleInfoList),"upload and add people");
        // Company step
        companyListDB = ((DashboardUI) UI.getCurrent()).companyService.findAll();
        wizard.addStep(new AddCompanyStep(companyListDB, companyInfoList), "Company");
        // Options step
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

    /**
     * Check steps for missing information(i.e. not uploaded file)
     * If there is a file, prepare the QR object and filter it
     * Then save it to the database
     * @param event
     */
    public void wizardCompleted(WizardCompletedEvent event) {
        try{
            if(fileInfo != null){
                if(fileInfo.getUrl() != null){
                    // Write file to the disk
                    fileInfo.writeByteToFile();
                    // Prepare QR object to be saved to the database
                    QR qr = prepareQRFields();
                    // save document to the database
                    ((DashboardUI) UI.getCurrent()).qrService.saveNewDocument(qr);
                    // TODO
                    // On successful save, go to the dashboard
                    QRGenerator.showGeneratedQR(qr);
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
        List<String> viewEmails = checkEmails(peopleInfoList, RightType.VIEW);
        List<String> editEmails = checkEmails(peopleInfoList, RightType.EDIT);

        List<String> viewCompanies = checkCompanies(companyInfoList, RightType.VIEW);
        List<String> editCompanies = checkCompanies(companyInfoList, RightType.EDIT);

        QR qr = new QR(fileInfo);

        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
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
     * Separates people info list that user has provided
     * into two lists, each consists of emails.
     * Separation is done with respect to right type.
     * @param peopleInfoList
     * @param rightType
     * @return
     */
    public List<String> checkEmails(List<PeopleInfo> peopleInfoList, RightType rightType){
        List<String> filteredEmails = new ArrayList<>();
        // traverse in the list
        for(PeopleInfo peopleInfo: peopleInfoList){
            // Check person's rights and given rights

            // If this is a edit list, we don't want to have
            // people with view right in this list
            if(rightType.equals(peopleInfo.getRightType()))
            {
                // Check validity last time
                boolean valid = EmailValidator.getInstance().isValid(peopleInfo.getEmail());
                if(valid){
                    filteredEmails.add(peopleInfo.getEmail());
                }
            }
        }
        return filteredEmails;
    }

    /**
     * TODO, ask company about user input
     * We have a list of company info. They are selected in the AddCompanyStep.
     * We have a list of company fetched from the database.
     * For each company info item, traverse the database list. Find equality of
     * companies with 2 helper functions. If the helper functions return true, that means
     * that the company is not modified with user input and can be added to the final list.
     *
     * One final thing, the company info's right type must be equal to the parameter
     * right type for the company to be added to the correct list.
     *
     * @param companyInfoList list of companies that user have selected
     * @param rightType edit or view
     * @return list of company email extension to be added to the QR
     */
    public List<String> checkCompanies(List<CompanyInfo> companyInfoList, RightType rightType){
        List<String> list = new ArrayList<>();

        for(CompanyInfo companyInfo: companyInfoList){
            // Check for user inputs
            // User might have changed it!
            for(int i = 0; i < companyListDB.size(); i++){
                // Check whether the company info is equal to the list item
                if(isCompanyNamesEqual(companyInfo,i)
                    && isCompanyEmailExtensionEqual(companyInfo, i)){
                    // Add to the list if the right type is satisfied
                    if(companyInfo.getRightType().equals(rightType)){
                        // add
                        list.add(companyInfo.getCompany().getEmailExtension());
                        break;
                        // go to the next company item and repeat
                    }
                }
            }
        }

       return list;
    }

    /**
     * Helper function for check emails
     * Checks names of list item and database list, with given index
     * @param companyInfo company info to be checked
     * @param i index in the companyListDB
     * @return true if the names are equals
     */
    private boolean isCompanyNamesEqual(CompanyInfo companyInfo, int i){
        return companyInfo.getCompanyName()
                .equals(companyListDB.get(i).getName());
    }

    /**
     * Helper function for check emails
     * Checks whether the email extension of the company that is selected
     * is equal to the database list item, with given index
     * @param companyInfo company info to be checked
     * @param i index of the database item
     * @return true if the email extensions are equal
     */
    private boolean isCompanyEmailExtensionEqual(CompanyInfo companyInfo, int i){
        return companyInfo.getCompany().getEmailExtension()
                .equals(companyListDB.get(i).getEmailExtension());
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
            if(fileInfo != null){
                if(fileInfo.getUrl() != null){
                    // delete the file on the disk
                    File toBeDeletedFile = new File(UploadAndAddPeople.uploadLocation + fileInfo.getDiskName());
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
