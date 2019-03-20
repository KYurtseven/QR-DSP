package com.qrsynergy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;

@Entity
@Document(collection = "COMPANY")
public class Company {

    private ObjectId _id;

    @Id
    @Indexed(unique = true)
    private String name;

    private String emailExtension;

    /**
     * Constructor for company
     * @param name
     * @param emailExtension
     */
    public Company(String name, String emailExtension){
        this.name = name.toUpperCase();
        this.emailExtension = emailExtension;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

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
