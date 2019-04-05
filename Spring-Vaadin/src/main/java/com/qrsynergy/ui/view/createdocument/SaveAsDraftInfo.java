package com.qrsynergy.ui.view.createdocument;

public class SaveAsDraftInfo {

    private Boolean isPublished;

    public SaveAsDraftInfo() {
        this.isPublished = true;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }
}
