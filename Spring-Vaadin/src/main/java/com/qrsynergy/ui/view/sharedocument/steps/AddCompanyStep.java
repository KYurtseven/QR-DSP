package com.qrsynergy.ui.view.sharedocument.steps;

import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

public class AddCompanyStep implements WizardStep {

    private String type;
    private HorizontalLayout content = new HorizontalLayout();
    private TwinColSelect<String> select;
    private Wizard owner;
    private Label titleLabel;

    /**
     * Constructor for Wizard
     *
     * @param type
     * view or edit
     * @param select
     * Twin column select component
     * @param owner
     * Parent wizard
     */
    public AddCompanyStep(String type,
                          TwinColSelect<String> select,
                          Wizard owner){
        this.type = type;
        this.select = select;
        this.owner = owner;
        if(type.equals("view")){
            titleLabel = new Label("Company view rights");
        }
        else{
            titleLabel = new Label("Company edit rights");
        }
    }

    /**
     * Shows user current step's name
     * @return current step's name
     */
    public String getCaption() {
        if(type.equals("view")){
            return "View";
        }
        return "Edit";
    }


    // TODO
    public Component getContent() {
        Responsive.makeResponsive(content);

        content.addComponents(titleLabel, select);

        return content;
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
