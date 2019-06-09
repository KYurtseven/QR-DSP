package com.qrsynergy.controller.helper;

/**
 * Sign up response
 */
public class SignUpResponse {

    private ResponseStatusType status;

    private FailureMessage message;

    /**
     * Empty constructor
     */
    public SignUpResponse(){

    }

    /**
     * Failure response,
     * failure in the message
     * @param message
     */
    public void failureSignUpResponse(FailureMessage message){
        this.status = ResponseStatusType.FAILURE;
        this.message = message;
    }

    /**
     * Success response, empty message part
     */
    public void successSignUpResponse(){
        this.status = ResponseStatusType.SUCCESS;
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
