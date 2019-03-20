package com.qrsynergy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import java.util.Date;

@Entity
@Document(collection = "USER")
public class User {

    private ObjectId _id;

    private String fullname;

    @Id
    @Indexed(unique = true)
    private String email;

    private String company;

    private String emailExtansion;

    // TODO
    // hash it
    private String password;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailExtansion() {
        return emailExtansion;
    }

    public void setEmailExtansion(String emailExtansion) {
        this.emailExtansion = emailExtansion;
    }

    public String toString(){
        return String.format("User{email: %s, company =%s}", email, company);
    }
}
