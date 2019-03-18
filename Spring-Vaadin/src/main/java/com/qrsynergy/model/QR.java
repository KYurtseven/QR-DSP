package com.qrsynergy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Document(collection = "QR")
public class QR {

    @Id
    private String url;
    // Owner
    private String o_info;

    private Boolean isPublic;
    // TODO,
    // enum
    // xlsx, pdf or dynamic form
    private String type;
    // list of company who can edit
    private List<CompanyScope> e_company;
    // list of company who can view
    private List<CompanyScope> v_company;
    // list of people who can edit
    private List<String> e_info;
    // list of people who can view
    private List<String> v_info;
    // QR's creation date
    private Date createdAt;
    // QR's last modified date
    private Date lastModified;
    // QR will not be available after this date.
    private Date expirationDate;

    // a name combined combined uuid
    private String diskName;
    // visible name
    private String originalName;

    @Override
    public String toString(){
        return String.format(
                "url: %s\n" +
                 "name: %s\n" ,
            url, originalName
        );
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getO_info() {
        return o_info;
    }

    public void setO_info(String o_info) {
        this.o_info = o_info;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CompanyScope> getE_company() {
        return e_company;
    }

    public void setE_company(List<CompanyScope> e_company) {
        this.e_company = e_company;
    }

    public List<CompanyScope> getV_company() {
        return v_company;
    }

    public void setV_company(List<CompanyScope> v_company) {
        this.v_company = v_company;
    }

    public List<String> getE_info() {
        return e_info;
    }

    public void setE_info(List<String> e_info) {
        this.e_info = e_info;
    }

    public List<String> getV_info() {
        return v_info;
    }

    public void setV_info(List<String> v_info) {
        this.v_info = v_info;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDiskName() {
        return diskName;
    }

    // TODO
    // Replace original file name with uuid, stores in that way
    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

}
