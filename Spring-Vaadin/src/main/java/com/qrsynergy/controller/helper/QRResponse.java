package com.qrsynergy.controller.helper;

import com.qrsynergy.model.helper.RightType;

/**
 * QR api response
 * TODO
 * This is not used for now. Use it
 */
public class QRResponse {
    private ResponseStatusType status;

    private FailureMessage message;

    private RightType rightType;

    // TODO

    /**
     * empty constructor
     */
    public QRResponse(){

    }

    public void failureQRResponse(FailureMessage message){
        this.status = ResponseStatusType.FAILURE;
        this.message = message;
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

    public RightType getRightType() {
        return rightType;
    }

    public void setRightType(RightType rightType) {
        this.rightType = rightType;
    }
}
