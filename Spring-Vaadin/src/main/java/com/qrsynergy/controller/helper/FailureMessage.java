package com.qrsynergy.controller.helper;

public enum FailureMessage {

    COMMENT_NOT_ENOUGH_RIGHTS("You don't have enough rights to comment on this document"),
    INVALID_EMAIL("Invalid email"),
    EMAIL_COMPANYMAIL_DONT_MATCH("Entered email and company email are not matched"),
    COMPANY_FIELD_IS_EMPTY("Company field is empty"),
    QR_EXPIRED("QR is expired"),
    QR_FILE_NOT_FOUND("File not found"),
    QR_NOT_FOUND("QR is not found"),
    QR_CANNOT_SEE_DOCUMENT("You cannot see this document"),
    SIGNUP_FIELDS_REQUIRED("Some fields are required"),
    SIGNUP_EMAIL_IN_USE("Email is in use"),
    LOGIN_INCORRECT_CREDENTIALS("Incorrect email or password"),
    UNKNOWN_ERROR("Unknown error is occured");

    private final String message;

    private FailureMessage(final String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
