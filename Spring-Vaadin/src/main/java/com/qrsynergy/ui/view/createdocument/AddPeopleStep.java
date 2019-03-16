package com.qrsynergy.ui.view.createdocument;

import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.ArrayList;
import java.util.List;

public class AddPeopleStep implements WizardStep {

    private String type;
    private List<String> emails;

    private VerticalLayout content;
    private TextField emailField = new TextField("Enter email address");


    public AddPeopleStep(String type, List<String> emails){
        this.type = type;
        this.emails = emails;
    }

    public String getCaption() {
        if(type.equals("view")){
            return "View rights";
        }
        return "Edit rights";
    }

    public Component getContent() {
        content = new VerticalLayout();
        content.setMargin(true);
        Button addToListButton= new Button("Add to list");
        addToListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // TODO
                // check correctness of the email
                emails.add(emailField.getValue());
                Label lastEmail = new Label(emailField.getValue());
                emailField.clear();
                content.addComponent(lastEmail);
            }
        });

        // TODO
        // add each email to a component
        // inside a component, there should be a button for removing the
        // email from the list and also from the UI
        for (String email:emails) {

        }
        content.addComponents(emailField, addToListButton);

        return content;
    }

    public boolean onAdvance() {
        return true;
    }

    public boolean onBack() {
        return true;
    }
}
