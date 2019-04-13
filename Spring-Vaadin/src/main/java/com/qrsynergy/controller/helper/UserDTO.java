package com.qrsynergy.controller.helper;

/**
 * Used for fetching request from REST API and the information
 * will be saved to the database
 */
public class UserDTO {

    private String fullName;

    private String email;

    private String company;

    private String password;

    /**
     *
     * @return full name of the user
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName full name of the user
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     *
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return company of the user
     */
    public String getCompany() {
        return company;
    }

    /**
     *
     * @param company company in the database
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     *
     * @return not hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password not hashed password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
