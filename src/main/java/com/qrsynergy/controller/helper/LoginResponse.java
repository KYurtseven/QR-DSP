package com.qrsynergy.controller.helper;

import com.qrsynergy.model.User;

public class LoginResponse {

    private ResponseStatusType status;

    private FailureMessage message;

    private String fullName;

    private String email;

    private String company;

    /**
     * empty constructor
     */
    public LoginResponse(){

    }

    public void successLoginResponse(User user){
        this.email  = user.getEmail();
        this.fullName = user.getFullName();
        this.company = user.getCompany();
        this.status = ResponseStatusType.SUCCESS;
    }

    public void failureLoginResponse(FailureMessage message){
        this.status = ResponseStatusType.FAILURE;
        this.message = message;
    }

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


    public ResponseStatusType getStatus() {
        return status;
    }

    public void setStatus(ResponseStatusType status) {
        this.status = status;
    }

    public FailureMessage getMessage() {
        return message;
    }

    public void setMessage(FailureMessage message) {
        this.message = message;
    }
}
