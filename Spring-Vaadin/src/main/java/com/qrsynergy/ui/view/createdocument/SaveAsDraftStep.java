package com.qrsynergy.ui.view.createdocument;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RadioButtonGroup;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Arrays;
import java.util.List;

public class SaveAsDraftStep implements WizardStep {

    private Wizard owner;
    private HorizontalLayout content = new HorizontalLayout();

    /**
     * Constructor
     * Wizard will be used to set published value of the document
     * @param owner
     */
    public SaveAsDraftStep(Wizard owner){
        this.owner = owner;
    }

    /**
     * Shows user current step's name
     * @return current step's name
     */
    public String getCaption() {
        return "Publish or draft";
    }


    @Override
    public Component getContent() {
        Responsive.makeResponsive(content);

        return content;
    }

    /**
     * It cannot go forward
     * @return
     */
    @Override
    public boolean onAdvance() {
        return false;
    }

    /**
     * It can go backwards
     * @return
     */
    @Override
    public boolean onBack() {
        return true;
    }
}
