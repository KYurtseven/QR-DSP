package com.qrsynergy.model;

import com.qrsynergy.controller.helper.UserDTO;
import com.qrsynergy.model.helper.Password;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;

@Entity
@Document(collection = "USER")
public class User {

    private ObjectId _id;

    private String fullName;

    @Id
    @Indexed(unique = true)
    private String email;

    private String company;

    private String emailExtension;

    private String salt;

    private String password;

    /**
     * empty constructor
     */
    public User(){

    }
    /**
     * Creates User from UserDTO
     * Hashes password
     * @param userDTO
     */
    public User(UserDTO userDTO){
        this.fullName = userDTO.getFullName();
        this.email = userDTO.getEmail();
        this.company = userDTO.getCompany();
        this.emailExtension = email.substring(email.lastIndexOf("@") + 1);

        byte[] salt = Password.generateSalt();
        byte[] hashedPasswordInByte = Password.hashPassword(userDTO.getPassword().toCharArray(), salt);

        this.salt = Password.bytetoString(salt);
        this.password = Password.bytetoString(hashedPasswordInByte);
    }

    /**
     * Checks the entered password and user's hashed password's equality
     * @return
     */
    public boolean isCorrectPassword(String enteredPassword){
        // hash entered password with stored salt
        byte[] hashedAndSaltedUserPassword = Password.hashPassword(
                enteredPassword.toCharArray(),
                Password.stringToByte(this.salt));

        // compare hashed password with calculated password
        if(Password.bytetoString(hashedAndSaltedUserPassword).equals(this.password)){
            return true;
        }
        return false;
    }

    /**
     *
     * @return Mongo db document id
     */
    public ObjectId get_id() {
        return _id;
    }

    /**
     * MongoRepository will set this, not used
     * @param _id mongo db document id
     */
    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    /**
     *
     * @return
     */
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getEmailExtension() {
        return emailExtension;
    }

    public void setEmailExtension(String emailExtension) {
        this.emailExtension = emailExtension;
    }

    public String toString(){
        return String.format("User{email: %s, company =%s}", email, company);
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

}
