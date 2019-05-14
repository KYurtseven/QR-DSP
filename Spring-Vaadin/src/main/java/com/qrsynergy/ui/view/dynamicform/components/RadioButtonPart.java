package com.qrsynergy.ui.view.dynamicform.components;

import javax.persistence.Entity;

import com.vaadin.ui.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.ElementCollection;

@Entity
public class RadioButtonPart extends FormPart {

    @ElementCollection
    List<String> radioButtonList = new ArrayList<String>();

    RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();

    private VerticalLayout verticalLayout;

    public RadioButtonPart(String title, Form parentForm) {
        this.title = title;
        this.parentForm = parentForm;
        // radioButtonGroup.setReadOnly(true);
        verticalLayout = new VerticalLayout();
    }

    public RadioButtonPart(JSONObject json, Form parentForm) {
        this.title = (String) json.get("label");
        this.parentForm = parentForm;
        JSONArray jArray = (JSONArray) json.get("radioNames");
        Iterator<String> iterator = jArray.iterator();
        while (iterator.hasNext()) {
            Add(iterator.next());
        }
        SetValue((String) json.get("selected"));
        verticalLayout = new VerticalLayout();
    }

    public void SetValue(int value) {
        radioButtonGroup.setValue(radioButtonList.get(value));
    }

    public void SetValue(String value) {
        radioButtonGroup.setValue(value);
    }

    public void Add(String checkboxString) {
        radioButtonList.add(checkboxString);
        radioButtonGroup.setItems(radioButtonList);
        SetValue(0);
    }

    public void AddToView(Layout layout) {
        if (parentForm.editable) {
            TextField textFieldTitle = new TextField();
            textFieldTitle.setValue(title);
            verticalLayout.addComponent(textFieldTitle);
            for (int i = 0; i < radioButtonList.size(); i++) {
                HorizontalLayout hl = new HorizontalLayout();
                TextField radiobuttonField = new TextField();
                Button delButton = new Button("Remove");
                radiobuttonField.setValue(radioButtonList.get(i));
                hl.addComponents(radiobuttonField, delButton);

                verticalLayout.addComponent(hl);
            }
            Button addButton = new Button("Add");
            verticalLayout.addComponent(addButton);
            layout.addComponent(verticalLayout);
        } else {
            Label div = new Label(title);

            verticalLayout.addComponents(div, radioButtonGroup);

            layout.addComponent(verticalLayout);
        }
    }

    public String ToJson() {
        StringBuilder str = new StringBuilder();
        int s = radioButtonList.size();
        str.append("{");
        str.append("label:\"");
        str.append(title);
        str.append("\",radioNames:[");
        for (int i = 0; i < s; i++) {
            str.append("\"");
            str.append(radioButtonList.get(i));
            str.append("\"");
            if (i != s - 1)
                str.append(",");
        }
        str.append("],selected:\"");
        str.append(radioButtonGroup.getValue());
        str.append("\"}");

        return str.toString();
    }

}
