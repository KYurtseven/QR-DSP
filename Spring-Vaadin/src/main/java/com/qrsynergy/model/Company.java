package com.qrsynergy.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "COMPANY")
public class Company {

    private String name;

    private String emailExtension;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailExtension() {
        return emailExtension;
    }

    public void setEmailExtension(String emailExtension) {
        this.emailExtension = emailExtension;
    }
}
