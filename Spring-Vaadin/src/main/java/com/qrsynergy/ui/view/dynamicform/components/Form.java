package com.qrsynergy.ui.view.dynamicform.components;

import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ElementCollection;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Entity
public class Form extends VerticalLayout {

    @Id
    @GeneratedValue
    private Long id;

    public String title;

    public boolean editable;

    @ElementCollection
    List<FormPart> formparts = new ArrayList<FormPart>();

    public void AddFormPart(FormPart fp){
        formparts.add(fp);
    }

    public void AddToView(FormPart fp){
        fp.AddToView(this);
    }

    public void InitView(Layout layout) {
        for (int i = 0; i < formparts.size(); i++) {
            formparts.get(i).AddToView(this);
        }
        layout.addComponent(this);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            return null;
        }
    }

    public void RemoveFormPart(FormPart fp){
        formparts.remove(fp);
    }

}
