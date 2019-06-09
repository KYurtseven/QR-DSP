package com.qrsynergy.ui.view.dynamicform.components;

import javax.persistence.Entity;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.ElementCollection;

@Entity
public class ImagePart extends FormPart{

    @ElementCollection
    List<String> labelList = new ArrayList<String>();
    @ElementCollection
    List<String> ImageList = new ArrayList<String>();

    private VerticalLayout verticalLayout;

    public ImagePart(String title,Form parentForm) {
        this.title = title;
        this.parentForm=parentForm;
        // radioButtonGroup.setReadOnly(true);
        verticalLayout = new VerticalLayout();
    }

    public ImagePart(JSONObject json,Form parentForm) {
        this.title = (String) json.get("label");
        this.parentForm=parentForm;
        JSONArray jArrayl = (JSONArray) json.get("labels");
        JSONArray jArrayi = (JSONArray) json.get("image_urls");
        Iterator<String> iteratorl = jArrayl.iterator();
        Iterator<String> iteratori = jArrayi.iterator();
        while (iteratorl.hasNext() && iteratori.hasNext()) {
            Add(iteratorl.next(), iteratori.next());
        }
        verticalLayout = new VerticalLayout();
    }

    public void SetValue(int value) {
        // radioButtonGroup.
    }

    public void Add(String labelString, String imageSource) {
        labelList.add(labelString);
        ImageList.add(imageSource);
    }

    public void AddToView(Layout layout) {
        Label div = new Label(title);
        verticalLayout.addComponent((Component) div);

        for (int i = 0; i < labelList.size(); i++) {
            Label labelDiv = new Label(labelList.get(i));

            Image image = new Image();
            image.setSource(new ExternalResource(ImageList.get(i)));
            image.setWidth("30%");
            verticalLayout.addComponents(image, (Component)labelDiv);
        }

        layout.addComponent(verticalLayout);
    }

    public String ToJson() {
        StringBuilder str = new StringBuilder();
        int s = labelList.size();
        str.append("{");
        str.append("label:\"");
        str.append(title);
        str.append("\",image_urls:[");
        for (int i = 0; i < s; i++) {
            str.append("\"");
            str.append(ImageList.get(i));
            str.append("\"");
            if (i != s - 1)
                str.append(",");
        }
        str.append("],labels:[");
        for (int i = 0; i < s; i++) {
            str.append("\"");
            str.append(labelList.get(i));
            str.append("\"");
            if (i != s - 1)
                str.append(",");
        }
        str.append("]}");

        return str.toString();
    }

}
