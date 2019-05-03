package com.qrsynergy.ui.view.sharedocument.steps;

import com.qrsynergy.ui.view.sharedocument.infos.AdditionalOptionsInfo;
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

    private VerticalLayout content;
    private AdditionalOptionsInfo additionalOptionsInfo;

    private final String optionDraft = "Save as draft";
    private final String optionPublishNow = "Publish now";

    private final String optionPublicYes = "Yes";
    private final String optionPublicNo = "No";


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
        content = new VerticalLayout();
        content.setSizeFull();
        content.addStyleName("text-center");

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.addStyleNames("text-center", "slot-text-center");
        wrapper.setId("wrapper");
        wrapper.setWidthUndefined();

        VerticalLayout wrapper2 = new VerticalLayout();
        wrapper2.setId("wrapper2");
        wrapper2.addComponent(addPublishRadioButton());
        wrapper2.addComponent(addPublicRadioButton());
        wrapper2.addComponent(addExpirationDate());

        wrapper.addComponent(wrapper2);
        content.addComponent(wrapper);
        return content;
    }

    /**
     * Adds date picker for expiration date
     * @return
     */
    private Component addExpirationDate(){

        DateField dateField = new DateField();

        dateField.setCaption("Expiration date");
        dateField.setValue(additionalOptionsInfo.getLocalDate());
        dateField.setDateFormat("yyyy-MM-dd");

        dateField.addValueChangeListener(event ->{
            // set expiration date, from local date to date
            additionalOptionsInfo.setExpirationDate(event.getValue());
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

        if(additionalOptionsInfo.isPublic()){
            publicRadio.setValue(optionPublicYes);
        }
        else{
            publicRadio.setValue(optionPublicNo);
        }

        publicRadio.setItems(optionPublicYes, optionPublicNo);

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

        if(additionalOptionsInfo.isPublished()){
            publishRadio.setValue(optionPublishNow);
        }
        else{
            publishRadio.setValue(optionDraft);
        }

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
