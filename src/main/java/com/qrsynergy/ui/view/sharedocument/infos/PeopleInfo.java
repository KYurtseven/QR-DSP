package com.qrsynergy.ui.view.sharedocument.infos;

import com.qrsynergy.model.helper.RightType;

/**
 * Class for storing emails and rights in the UploadAndAddPeopleStep
 */
public class PeopleInfo {

    private String email;

    private RightType rightType;

    /**
     * Creates people info with VIEW type
     * @param email
     */
    public PeopleInfo(String email){
        this.email = email;
        this.rightType = RightType.VIEW;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RightType getRightType() {
        return rightType;
    }

    public void setRightType(RightType rightType) {
        this.rightType = rightType;
    }
}
