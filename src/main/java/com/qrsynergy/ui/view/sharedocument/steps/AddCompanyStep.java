package com.qrsynergy.ui.view.sharedocument.steps;

import com.qrsynergy.model.Company;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.ui.view.sharedocument.infos.CompanyInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.ArrayList;
import java.util.List;

public class AddCompanyStep implements WizardStep {

    private VerticalLayout content;

    private List<Company> companyListDB;
    private List<CompanyInfo> companyInfoList;
    private Grid<CompanyInfo> grid;
    private final int maxRowCount = 8;
    private final int initialGridWith = 420;
    /**
     *
     * @param companyListDB
     * @param companyInfoList
     */
    public AddCompanyStep(List<Company> companyListDB, List<CompanyInfo> companyInfoList){
        this.companyListDB = companyListDB;
        this.companyInfoList = companyInfoList;
    }

    /**
     * Shows user current step's name
     * @return current step's name
     */
    public String getCaption() {
        return "Company";
    }

    /**
     * Builds content
     * @return
     */
    public Component getContent() {
        content = new VerticalLayout();
        content.setSizeFull();
        content.addStyleName("text-center");

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.addStyleNames( "text-center", "slot-text-center");

        wrapper.addComponent(buildComboBoxLayout());
        wrapper.addComponent(buildGrid());
        content.addComponent(wrapper);
        return content;
    }

    /**
     * Builds combo box and add button
     * @return
     */
    private Component buildComboBoxLayout(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleNames("text-center", "slot-text-center");

        ComboBox<Company> comboBox = new ComboBox<>("Select a company to add");
        comboBox.setItems(companyListDB);
        // Company names in the combobox
        comboBox.setItemCaptionGenerator(company -> company.getName());

        Button addCompanyButton = new Button("Add");
        addCompanyButton.addStyleName("margin-top-25");

        // adds company to the grid
        addCompanyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // avoid empty click
                if(comboBox.getSelectedItem().isPresent()){
                    CompanyInfo companyInfo = new CompanyInfo(comboBox.getSelectedItem().get());
                    // Avoid re-adding
                    if(!isAlreadyAdded(companyInfo)){
                        // Not in the list, add
                        companyInfoList.add(companyInfo);
                        // Resize the grid if the max count is exceeded
                        if(companyInfoList.size() == maxRowCount + 1){
                            grid.setWidth(grid.getWidth() + 10, Sizeable.Unit.PIXELS);
                        }
                        // refresh grid
                        grid.getDataProvider().refreshAll();
                    }
                }
            }
        });

        layout.addComponents(comboBox, addCompanyButton);
        return layout;
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

    /**
     * Builds grid
     * @return company adding grid
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

        grid.addComponentColumn(companyInfo -> buildRemoveCompanyButton(companyInfo))
                .setCaption("Remove")
                .setId("remove")
                .setWidth(100)
                .setResizable(false)
                .setSortable(false);

        grid.setWidth(initialGridWith, Sizeable.Unit.PIXELS);
        // Set fixed max height for rows
        grid.setHeightByRows(maxRowCount);

        grid.addStyleName("slot-text-center");


        return grid;
    }

    /**
     * Changes company's rights on the QR
     * @param companyInfo rights of the company to be changed
     * @return radio button for each company info
     */
    private Component buildRightsRadio(CompanyInfo companyInfo){
        RadioButtonGroup<RightType> radio = new RadioButtonGroup<>();

        radio.setItems(RightType.EDIT, RightType.VIEW);
        radio.setValue(companyInfo.getRightType());

        radio.addValueChangeListener(event->{
            if(event.getValue().equals(RightType.EDIT)){
                companyInfo.setRightType(RightType.EDIT);
            }
            else{
                companyInfo.setRightType(RightType.VIEW);
            }
        });

        radio.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        return radio;
    }


    /**
     * Clicking this button removes the company from the grid
     * @param companyInfo company to be removed
     * @return button
     */
    private Component buildRemoveCompanyButton(CompanyInfo companyInfo){
        Button removeButton = new Button();
        removeButton.setIcon(VaadinIcons.TRASH);

        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                companyInfoList.remove(companyInfo);
                if(companyInfoList.size() == maxRowCount - 1){
                    grid.setWidth(grid.getWidth() - 10, Sizeable.Unit.PIXELS);
                }
                grid.getDataProvider().refreshAll();
            }
        });

        return removeButton;

    }

    /**
     * It can go forward
     * @return
     */
    public boolean onAdvance() {
        return true;
    }

    /**
     * It can go backwards
     * @return
     */
    public boolean onBack() {
        return true;
    }
}
