package com.qrsynergy.controller.helper;

public class SignUpResponse {

    private ResponseStatusType status;

    private FailureMessage message;

    public void failureSignUpResponse(FailureMessage message){
        this.status = ResponseStatusType.FAILURE;
        this.message = message;
    }

    public void successSignUpResponse(){
        this.status = ResponseStatusType.SUCCESS;
    }
}
