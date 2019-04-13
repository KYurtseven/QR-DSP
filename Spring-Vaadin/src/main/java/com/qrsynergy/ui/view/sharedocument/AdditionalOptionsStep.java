package com.qrsynergy.ui.view.sharedocument;

import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.wizards.WizardStep;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Options for the document:
 * Save as draft, select expiration date, select public/not public.
 */
public class AdditionalOptionsStep implements WizardStep {

    private VerticalLayout content = new VerticalLayout();
    private AdditionalOptionsInfo additionalOptionsInfo;

    private final String optionDraft = "Save as draft";
    private final String optionPublishNow = "Publish now";

    private final String optionPublicYes = "Yes";
    private final String optionPublinNo = "No";


    /**
     * Constructor
     * Wizard will be used to set published value of the document
     * @param additionalOptionsInfo value to set CreateDocumentView
     */
    public AdditionalOptionsStep(AdditionalOptionsInfo additionalOptionsInfo){
        this.additionalOptionsInfo = additionalOptionsInfo;
    }

    /**
     * Shows user current step's name
     * @return current step's name
     */
    public String getCaption() {
        return "Options";
    }


    /**
     * Builds options fields
     * Draft/publish, public/not public, expiration date
     * @return
     */
    @Override
    public Component getContent() {
        Responsive.makeResponsive(content);

        content.addComponent(addPublishRadioButton());
        content.addComponent(addPublicRadioButton());
        content.addComponent(addExpirationDate());

        return content;
    }

    /**
     * Adds date picker for expiration date
     * @return
     */
    private Component addExpirationDate(){

        HorizontalLayout row = new HorizontalLayout();

        DateField dateField = new DateField();
        dateField.setValue(LocalDate.MIN);
        dateField.setDateFormat("yyyy-MM-dd");

        dateField.addValueChangeListener(event ->{

            Date date = Date.from(event.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            additionalOptionsInfo.setExpirationDate(date);

        });

        return dateField;
    }

    /**
     * Adds radio button for public/not public to the content
     * @return
     */
    private Component addPublicRadioButton(){
        RadioButtonGroup<String> publicRadio = new RadioButtonGroup<>
                ("Is this document public accessible?");

        publicRadio.setValue(optionPublinNo);
        publicRadio.setItems(optionPublicYes, optionPublinNo);

        publicRadio.addValueChangeListener(event ->{

            if(event.getValue().equals(optionPublicYes)){
                additionalOptionsInfo.setPublic(true);
            }
            else{
                additionalOptionsInfo.setPublic(false);
            }
        });

        publicRadio.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        return publicRadio;
    }

    /**
     * Adds radio button for publish/draft to the content
     * @return radio button
     */
    private Component addPublishRadioButton(){
        RadioButtonGroup<String> publishRadio = new RadioButtonGroup<>
                ("Please, select publish method");

        publishRadio.setValue(optionPublishNow);
        publishRadio.setItems(optionDraft, optionPublishNow);

        publishRadio.addValueChangeListener(event->{
            if(event.getValue().equals(optionDraft)){
                additionalOptionsInfo.setPublished(false);
            }
            else{
                additionalOptionsInfo.setPublished(true);
            }
        });

        publishRadio.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        return publishRadio;
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
