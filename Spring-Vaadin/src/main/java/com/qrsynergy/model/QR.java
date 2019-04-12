package com.qrsynergy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

/**
 * QR entity in the database
 */
@Entity
@Document(collection = "QR")
public class QR {

    private ObjectId _id;

    @Id
    @Indexed(unique = true)
    private String url;

    private String o_info;

    private Boolean isPublic;

    private Boolean isPublished;

    // TODO,
    // enum
    // xlsx, pdf or dynamic form
    private String type;

    private List<String> e_company;

    private List<String > v_company;

    private List<String> e_info;

    private List<String> v_info;

    private Date createdAt;

    private Date lastModified;

    private Date expirationDate;

    private String diskName;

    private String originalName;

    @Override
    public String toString(){
        return String.format(
                "url: %s\n" +
                 "name: %s\n" ,
            url, originalName
        );
    }

    /**
     *
     * @return url, unique id of the document
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets unique id of the document
     * used for creation of the document
     * @param url uuid
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return owner's email
     */
    public String getO_info() {
        return o_info;
    }

    /**
     *
     * @param o_info owner's email
     */
    public void setO_info(String o_info) {
        this.o_info = o_info;
    }

    /**
     *
     * @return can everyone see the document?
     */
    public Boolean getPublic() {
        return isPublic;
    }

    /**
     *
     * @param aPublic set visibility to everyone
     */
    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    /**
     * TODO
     * right now, it is xlsx
     * @return type of the document
     */
    public String getType() {
        return type;
    }

    /**
     * TODO
     * right now, it is xlsx
     * @param type type of the document, xlsx, pdf or dynamicform
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return list of email extension's of companies who can edit
     */
    public List<String> getE_company() {
        return e_company;
    }

    /**
     *
     * @param e_company list of email extension's of companies
     */
    public void setE_company(List<String> e_company) {
        this.e_company = e_company;
    }

    /**
     *
     * @return list of email extension's of companies who can view
     */
    public List<String> getV_company() {
        return v_company;
    }

    /**
     *
     * @param v_company list of email extension's of companies
     */
    public void setV_company(List<String> v_company) {
        this.v_company = v_company;
    }

    /**
     *
     * @return list of email's of users who can edit
     */
    public List<String> getE_info() {
        return e_info;
    }

    /**
     *
     * @param e_info list of email's of users
     */
    public void setE_info(List<String> e_info) {
        this.e_info = e_info;
    }

    /**
     *
     * @return list of email's of users who can view
     */
    public List<String> getV_info() {
        return v_info;
    }

    /**
     *
     * @param v_info list of email's of users
     */
    public void setV_info(List<String> v_info) {
        this.v_info = v_info;
    }

    /**
     *
     * @return QR's creation date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt QR's creation date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return QR's last modified date
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     *
     * @param lastModified QR's last modified date
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * QR should not be opened after this date
     * @return QR's expiration date
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * QR should not be opened after this date
     * @param expirationDate QR's expiration date
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * a name combined combined uuid
     * uuid field is the same as QR's uuid/url
     * ex: "02cbeaa2-7b36-4207-91d9-dabffe7f4232.xlsx"
     * Used for opening document in the web
     * @return disk name of the QR
     */
    public String getDiskName() {
        return diskName;
    }

    /**
     * Replaces file's original name with uuid.xlsx
     * to avoid files with same names
     * ex: "02cbeaa2-7b36-4207-91d9-dabffe7f4232.xlsx"
     * @param diskName uuid + type, uuid is url of the document, type is excel, pdf
     */
    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    /**
     * ex: "23-24 subat egitmen semineri.xlsx"
     * User will see this name when he fetches his documents
     * @return meaningful name to the user
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * ex: "23-24 subat egitmen semineri.xlsx"
     * @param originalName original name of the document
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    /**
     * Only owner can see the document, even if the document is not published
     *
     * @return Is it published?
     */
    public Boolean getPublished() {
        return isPublished;
    }

    /**
     *
     * @param published true for publish now, false for draft
     */
    public void setPublished(Boolean published) {
        isPublished = published;
    }

    /**
     *
     * @return object id of the mongodb object
     */
    public ObjectId get_id() {
        return _id;
    }

    /**
     * This method will be used from mongorepository
     * @param _id object id of the mongodb object
     */
    public void set_id(ObjectId _id) {
        this._id = _id;
    }

}
