package com.qrsynergy.ui.view.createdocument;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Arrays;
import java.util.List;

public class SaveAsDraftStep implements WizardStep {

    private HorizontalLayout content = new HorizontalLayout();
    private boolean isPublished;

    private final String optionDraft = "Save as draft";
    private final String optionPublishNow = "Publish now";

    /**
     * Constructor
     * Wizard will be used to set published value of the document
     * @param isPublished value to set CreateDocumentView
     */
    public SaveAsDraftStep(boolean isPublished){
        this.isPublished = isPublished;
    }

    /**
     * Shows user current step's name
     * @return current step's name
     */
    public String getCaption() {
        return "Publish or draft";
    }


    /**
     * Adds radio buttons to the content
     * @return
     */
    @Override
    public Component getContent() {
        Responsive.makeResponsive(content);

        RadioButtonGroup<String> radioButton = new RadioButtonGroup<>
                ("Please, select publish method");

        radioButton.setValue(optionPublishNow);
        radioButton.setItems(optionDraft, optionPublishNow);

        radioButton.addValueChangeListener(event->{
            if(event.getValue().equals(optionDraft)){
                isPublished = false;
            }
            else{
                isPublished = true;
            }
        });

        radioButton.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

        content.addComponent(radioButton);
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
