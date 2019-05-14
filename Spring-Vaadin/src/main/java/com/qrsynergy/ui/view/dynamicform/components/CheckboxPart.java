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
public class CheckboxPart extends FormPart {

    @ElementCollection
    List<CheckBox> checkboxList = new ArrayList<CheckBox>();

    private VerticalLayout verticalLayout;

    public CheckboxPart(String title, Form parentForm) {
        this.title = title;
        this.parentForm = parentForm;
        verticalLayout = new VerticalLayout();
    }

    public CheckboxPart(JSONObject json, Form parentForm) {
        this.title = (String) json.get("label");
        this.parentForm = parentForm;
        JSONArray jArray = (JSONArray) json.get("buttons");
        Iterator<JSONObject> iterator = jArray.iterator();
        while (iterator.hasNext()) {
            JSONObject jo = iterator.next();
            Add((String) jo.get("checkName"), (Boolean) jo.get("checked"));
        }
        verticalLayout = new VerticalLayout();
    }




    public void Add(String checkboxString, Boolean value) {
        CheckBox checkbox = new CheckBox(checkboxString, value);
        checkboxList.add(checkbox);
    }

    public void Remove(int c) {
        checkboxList.remove(c);
        showNotification("Removed " + c);
    }

    public void ClearView(Layout layout) {
        layout.removeComponent(verticalLayout);
        verticalLayout.removeAllComponents();
    }

    public void AddToView(Layout layout) {
        if (parentForm.editable) {
            TextField textFieldTitle = new TextField();
            textFieldTitle.setValue(title);
            verticalLayout.addComponent(textFieldTitle);
            for (int i = 0; i < checkboxList.size(); i++) {
                HorizontalLayout hl = new HorizontalLayout();
                TextField checkboxField = new TextField();
                CheckBox checkbox = new CheckBox();
                checkboxField.setValue(checkboxList.get(i).getCaption());
                final int checkbox_idx = i;
                Button delButton = new Button("Remove");
                delButton.addClickListener(event -> {
                    Remove(checkbox_idx);
                    showNotification("Removed " + checkbox.getCaption());
                    ClearView(layout);
                    AddToView(layout);
                });
                hl.addComponents(checkbox, checkboxField, delButton);

                verticalLayout.addComponent(hl);
            }
            Button addButton = new Button("Add");
            addButton.addClickListener(event -> {
                Add("Dummy", false);
                ClearView(layout);
                AddToView(layout);
            });
            Button removeButton = new Button("Remove Part");
            removeButton.addClickListener(event -> {
                ClearView(layout);
                parentForm.RemoveFormPart(this);
            });
            verticalLayout.addComponents(addButton, removeButton);

            layout.addComponent(verticalLayout);
        } else {
            Label div = new Label(title);

            //div.setClassName(title);
            //div.setText(title);
            verticalLayout.addComponent(div);
            for (int i = 0; i < checkboxList.size(); i++) {
                verticalLayout.addComponent(checkboxList.get(i));
            }
            layout.addComponent(verticalLayout);
        }
    }

    public String ToJson() {
        StringBuilder str = new StringBuilder();
        int s = checkboxList.size();
        str.append("{");
        str.append("label:\"");
        str.append(title);
        str.append("\",buttons:[");
        for (int i = 0; i < s; i++) {
            str.append("{checkName:\"");
            str.append(checkboxList.get(i).getCaption());
            str.append("\",checked:");
            str.append(checkboxList.get(i).getValue());
            str.append("}");
            if (i != s - 1)
                str.append(",");
        }
        str.append("]}");

        return str.toString();
    }

}
