package com.qrsynergy.controller.helper;

public enum FailureMessage {
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
}
