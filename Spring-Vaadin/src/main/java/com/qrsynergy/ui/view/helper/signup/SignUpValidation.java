package com.qrsynergy.ui.view.helper.signup;


/**
 * Class used for showing notification to the user
 */
public class SignUpValidation {

    private final boolean isValid;

    private final SignUpErrorType errorMessage;

    /**
     * Constructor
     * @param isValid valid or not
     * @param errorMessage message why it is not valid
     */
    public SignUpValidation(boolean isValid, SignUpErrorType errorMessage){
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    /**
     * @return true if valid
     */
    public boolean isValid(){
        return this.isValid;
    }

    /**
     *
     * @return error message
     */
    public String getErrorMessage(){
        return this.errorMessage.getMessage();
    }
}
