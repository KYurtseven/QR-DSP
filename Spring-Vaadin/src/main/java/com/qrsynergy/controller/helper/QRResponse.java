package com.qrsynergy.controller.helper;

public class QRResponse {
    private ResponseStatusType status;

    private FailureMessage message;

    private QRAccessType accessType;

    // TODO

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

    public QRAccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(QRAccessType accessType) {
        this.accessType = accessType;
    }

}
