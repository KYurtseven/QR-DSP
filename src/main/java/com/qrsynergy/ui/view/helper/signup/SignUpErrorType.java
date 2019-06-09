package com.qrsynergy.ui.view.helper.signup;

/**
 * Enum used for error in sign up
 */
public enum SignUpErrorType {
    OK("OK"),
    NOT_VALID_NAME("Please enter a valid name"),
    NOT_SELECTED_COMPANY("Please select a company"),
    EMPTY_PASSWORD("Please provide a password"),
    SHORT_PASSWORD("Given password is too short"),
    PASSWORD_NOT_MATCH("Passwords are not matched"),
    EMAIL_COMPANY_EMAIL_NOT_MATCH("You cannot select this company"),
    INVALID_EMAIL("Provided email is not valid");

    private final String message;

    private SignUpErrorType(final String message){
        this.message = message;
    }

    /**
     *
     * @return meaningful message
     */
    public String getMessage(){
        return message;
    }
}