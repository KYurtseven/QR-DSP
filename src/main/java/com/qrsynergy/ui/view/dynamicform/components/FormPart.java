package com.qrsynergy.ui.view.dynamicform.components;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.qrsynergy.ui.view.helper.ShowNotification;
import com.vaadin.ui.Layout;

@Entity
public abstract class FormPart {

    @Id
    @GeneratedValue
    private Long id;

    public String title;

    public Form parentForm;

    public void showNotification(String text) {
        ShowNotification.showNotification(text);
    }
    
    FormPart() {

    }

    public abstract void  AddToView(Layout v);
}
