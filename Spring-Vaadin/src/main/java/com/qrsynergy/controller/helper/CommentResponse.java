package com.qrsynergy.controller.helper;

public class CommentResponse {

    private ResponseStatusType status;

    private String message;

    /**
     * Empty constructor
     */
    public CommentResponse(){

    }

    /**
     * Constructor
     * @param message
     */
    public CommentResponse(FailureMessage message){
        this.status = ResponseStatusType.FAILURE;
        this.message = message.getMessage();
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
