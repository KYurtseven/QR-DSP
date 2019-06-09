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
public class ListPart extends FormPart {

    @ElementCollection
    List<String> textList = new ArrayList<String>();

    private VerticalLayout verticalLayout;

    public ListPart(String title,Form parentForm) {
        this.title = title;
        this.parentForm=parentForm;
        // radioButtonGroup.setReadOnly(true);
        verticalLayout = new VerticalLayout();
    }

    public ListPart(JSONObject json,Form parentForm) {
        this.title = (String) json.get("label");
        this.parentForm=parentForm;
        JSONArray jArray = (JSONArray)json.get("texts");
        Iterator<String> iterator = jArray.iterator();
            while (iterator.hasNext()) {
                Add(iterator.next());
            }
        verticalLayout = new VerticalLayout();
    }

    public void SetValue(int value) {
        // radioButtonGroup.
    }

    public void Add(String text) {
        textList.add(text);
    }

    public void AddToView(Layout layout) {
        Label div = new Label(title);

        verticalLayout.addComponent(div);

        for (int i = 0; i < textList.size(); i++) {
            TextField textField = new TextField();
            textField.setValue(textList.get(i));
            // textField.setSizeFull();
            textField.setWidth("50%");
            textField.setMaxLength(50);
            verticalLayout.addComponent(textField);
        }

        layout.addComponent(verticalLayout);
    }

    public String ToJson() {
        StringBuilder str = new StringBuilder();
        int s = textList.size();
        str.append("{");
        str.append("label:\"");
        str.append(title);
        str.append("\",texts:[");
        for (int i = 0; i < s; i++) {
            str.append("\"");
            str.append(textList.get(i));
            str.append("\"");
            if (i != s - 1)
                str.append(",");
        }
        str.append("]}");

        return str.toString();
    }

}
