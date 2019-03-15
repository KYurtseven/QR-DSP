package com.qrsynergy.view.generateqr;


import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

public class DemoLabel extends Label {

    public DemoLabel(String html) {
        super(html, ContentMode.HTML);
        setWidth("100%");
    }

}