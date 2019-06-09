package com.qrsynergy.ui.view.viewdocument.tabs;

import com.qrsynergy.model.Company;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.service.QRService;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.qrsynergy.ui.view.sharedocument.infos.CompanyInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EditCompanyRights {

    private VerticalLayout content;
    private QR qr;
    private Grid<CompanyInfo> grid;
    private RightType rightType;
    private final int maxRowCount = 6;
    private final int initialGridWith;
    private ComboBox<Company> select;
    private final List<Company> companyListDB;
    private List<CompanyInfo> companyInfoList;

    /**
     * Constructor
     * @param qr QR
     */
    public EditCompanyRights(QR qr, RightType rightType){
        this.qr = qr;
        // Only owner right should edit company rights
        this.rightType = rightType;
        if(rightType.equals(RightType.OWNER)){
            // Have remove button, more size
            initialGridWith = 420;
        }
        else{
            initialGridWith = 320;
        }
        // fill combobox
        companyListDB = ((DashboardUI) UI.getCurrent()).companyService.findAll();
        initCompanyInfoList();

    }


    /**
     * Initializes company info list
     * Traverse from edit company list. For each email extension, create a
     * CompanyInfo object with edit right and append to the companyInfoList.
     * Do the same for the view company list.
     */
    private void initCompanyInfoList(){
        companyInfoList = new ArrayList<>();

        for(String emailExtension: qr.getE_company()){
            for(Company company: companyListDB){
                if(company.getEmailExtension().equals(emailExtension)){
                    // initialize companyInfo
                    CompanyInfo companyInfo = new CompanyInfo(company);
                    companyInfo.setRightType(RightType.EDIT);
                    companyInfoList.add(companyInfo);
                    break;
                }
            }
        }
        for(String emailExtension: qr.getV_company()){
            for(Company company: companyListDB){
                if(company.getEmailExtension().equals(emailExtension)){
                    CompanyInfo companyInfo = new CompanyInfo(company);
                    companyInfo.setRightType(RightType.VIEW);
                    companyInfoList.add(companyInfo);
                    break;
                }
            }
        }
    }


    // TODO
    public Component getContent() {
        content = new VerticalLayout();
        content.setSizeFull();
        content.addStyleName("text-center");

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.addStyleNames("text-center", "slot-text-center");


        // Only owner can add and remove companies
        if(rightType.equals(RightType.OWNER))
            wrapper.addComponent(buildAddCompanyLayout());

        wrapper.addComponent(buildGrid());

        content.addComponent(wrapper);
        return content;
    }

    /**
     * Builds grid
     * @return grid
     */
    private Component buildGrid(){
        grid = new Grid<>();
        grid.setItems(companyInfoList);
        grid.removeAllColumns();

        grid.addColumn(CompanyInfo::getCompanyName)
                .setCaption("Company")
                .setId("name")
                .setWidth(150)
                .setResizable(false)
                .setSortable(true);

        grid.addComponentColumn(companyInfo -> buildRightsRadio(companyInfo))
                .setCaption("Rights")
                .setWidth(170)
                .setId("radio")
                .setResizable(false)
                .setSortable(false);

        if(rightType.equals(RightType.OWNER)){
            grid.addComponentColumn(companyInfo -> buildRemoveCompanyButton(companyInfo))
                    .setCaption("Remove")
                    .setId("remove")
                    .setWidth(100)
                    .setResizable(false)
                    .setSortable(false);
        }


        grid.setWidth(initialGridWith, Sizeable.Unit.PIXELS);
        // Set fixed max height for rows
        grid.setHeightByRows(maxRowCount);

        grid.addStyleName("slot-text-center");


        return grid;
    }

    /**
     * Changes company's rights on the QR
     * Updates the grid and also the database
     * @param companyInfo rights of the company to be changed
     * @return radio button for each company info
     */
    private Component buildRightsRadio(CompanyInfo companyInfo){
        RadioButtonGroup<RightType> radio = new RadioButtonGroup<>();

        radio.setItems(RightType.EDIT, RightType.VIEW);
        radio.setValue(companyInfo.getRightType());

        radio.addValueChangeListener(event->{
            radio.setEnabled(false);
            if(event.getValue().equals(RightType.EDIT)){
                companyInfo.setRightType(RightType.EDIT);
                // save to the database
                // old right is view
                boolean result = ((DashboardUI) UI.getCurrent()).qrService
                        .changeRightOfCompany(qr, companyInfo.getCompany().getEmailExtension(), RightType.VIEW);
                if(result){
                    ShowNotification.showNotification(companyInfo.getCompanyName() + "'s right is changed from View to Edit");
                }
                else{
                    ShowNotification.showWarningNotification("Error on changing right of the company " + companyInfo.getCompanyName());
                }
            }
            else{
                companyInfo.setRightType(RightType.VIEW);
                // Save to the database
                // old right is edit
                boolean result = ((DashboardUI) UI.getCurrent()).qrService
                        .changeRightOfCompany(qr, companyInfo.getCompany().getEmailExtension(), RightType.EDIT);
                if(result){
                    ShowNotification.showNotification(companyInfo.getCompanyName() + "'s right is changed from Edit to View");
                }
                else{
                    ShowNotification.showWarningNotification("Error on changing right of the company " + companyInfo.getCompanyName());
                }
            }
            radio.setEnabled(true);
        });

        radio.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

        if(rightType != RightType.OWNER){
            radio.setReadOnly(true);
        }
        return radio;
    }

    /**
     * Clicking this button removes the company from the grid and from the database
     * @param companyInfo company to be removed
     * @return button
     */
    private Component buildRemoveCompanyButton(CompanyInfo companyInfo){
        Button removeButton = new Button();
        removeButton.setIcon(VaadinIcons.TRASH);

        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                boolean result = ((DashboardUI) UI.getCurrent()).qrService
                        .removeCompanyFromQR(qr, companyInfo.getCompany().getEmailExtension());
                if(result){
                    companyInfoList.remove(companyInfo);
                    if(companyInfoList.size() == maxRowCount - 1){
                        grid.setWidth(grid.getWidth() - 10, Sizeable.Unit.PIXELS);
                    }
                    grid.getDataProvider().refreshAll();
                    ShowNotification.showNotification(companyInfo.getCompanyName() + " is removed from the QR");
                }
                else{
                    ShowNotification.showWarningNotification("Error on removing company");
                }
            }
        });

        return removeButton;

    }

    /**
     * Similar to the addCompanyStep, build a add company layout
     * It has a combobox containing list of companies, an add button to add
     * to the list. Whenever the add button is pressed, the company is immediately
     * added to both database and grid on successful execution.
     * Don't add already added companies
     * @return layout containing combobox and button
     */
    private Component buildAddCompanyLayout(){

        HorizontalLayout hl = new HorizontalLayout();
        hl.addStyleNames("text-center", "slot-text-center");

        select = new ComboBox<>("Select a company to add");
        select.setItems(companyListDB);
        select.setItemCaptionGenerator(company -> company.getName());

        Button addCompanyButton = new Button("Add");
        addCompanyButton.addStyleName("margin-top-25");

        addCompanyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(select.getSelectedItem().isPresent()){
                    // default right is view
                    CompanyInfo companyInfo = new CompanyInfo(select.getSelectedItem().get());
                    if(!isAlreadyAdded(companyInfo)){

                        // Save to the database
                        boolean result = ((DashboardUI) UI.getCurrent()).qrService
                                .addNewCompanyToQR(qr, companyInfo.getCompany().getEmailExtension());
                        if(result){
                            // Not in the list, add
                            companyInfoList.add(companyInfo);
                            // Resize the grid if the max count is exceeded
                            if(companyInfoList.size() == maxRowCount + 1){
                                grid.setWidth(grid.getWidth() + 10, Sizeable.Unit.PIXELS);
                            }
                            // refresh grid
                            grid.getDataProvider().refreshAll();
                        }
                        else{
                            ShowNotification.showWarningNotification("Error during adding new company to the list");
                        }
                    }
                }

            }
        });
        hl.addComponents(select, addCompanyButton);
        return hl;
    }

    /**
     * If the company is already added to the grid,
     * returns true.
     * Used for not re-adding the same company in the list again and again
     * @param rhs company info to be added to the grid
     * @return true if already exists in the grid, false otherwise
     */
    private boolean isAlreadyAdded(CompanyInfo rhs){
        for(CompanyInfo companyInfo: companyInfoList){
            if(rhs.getCompanyName().equals(companyInfo.getCompanyName())){
                return true;
            }
        }
        return false;
    }
}
