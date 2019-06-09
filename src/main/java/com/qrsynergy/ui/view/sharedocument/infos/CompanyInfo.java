package com.qrsynergy.ui.view.sharedocument.infos;

import com.qrsynergy.model.Company;
import com.qrsynergy.model.helper.RightType;

public class CompanyInfo {

    private Company company;
    private String companyName;

    private RightType rightType;

    /**
     * Creates company info with VIEW type
     * @param company
     */
    public CompanyInfo(Company company){
        this.company = company;
        this.companyName = company.getName();
        this.rightType = RightType.VIEW;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public RightType getRightType() {
        return rightType;
    }

    public void setRightType(RightType rightType) {
        this.rightType = rightType;
    }

}
