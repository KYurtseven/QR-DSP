package com.qrsynergy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Document(collection = "QR")
public class QR {

    private ObjectId _id;

    @Id
    @Indexed(unique = true)
    private String url;
    // Owner
    private String o_info;

    private Boolean isPublic;

    private Boolean isPublished;
    // TODO,
    // enum
    // xlsx, pdf or dynamic form
    private String type;
    // list of company who can edit
    // includes companies email extension
    private List<String> e_company;
    // list of company who can view
    // includes companies email extension
    private List<String > v_company;
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

    public List<String> getE_company() {
        return e_company;
    }

    public void setE_company(List<String> e_company) {
        this.e_company = e_company;
    }

    public List<String> getV_company() {
        return v_company;
    }

    public void setV_company(List<String> v_company) {
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

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }
}
