package com.qrsynergy.model;

import com.qrsynergy.model.helper.DocumentType;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.model.helper.UserDocument;
import com.qrsynergy.ui.view.sharedocument.infos.FileInfo;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Calendar;
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

    private DocumentType documentType;

    private List<String> e_company;

    private List<String > v_company;

    private List<String> e_info;

    private List<String> v_info;

    private Date creationDate;

    private Date lastModified;

    private Date expirationDate;

    private String diskName;

    private String originalName;

    /**
     *
     * @return true if expired, expiration date is less than now
     */
    public boolean isExpired(){
        return(this.expirationDate.getTime() < Calendar.getInstance().getTime().getTime());
    }


    /**
     * empty constructor
     */
    public QR(){

    }

    /**
     * Creates QR from first step info (file upload step)
     * @param fileInfo file and file upload information
     */
    public QR(FileInfo fileInfo){
        this.url = fileInfo.getUrl();
        this.documentType = fileInfo.getDocumentType();
        this.originalName = fileInfo.getOriginalName();
        this.creationDate = fileInfo.getCreationDate();
        this.lastModified = fileInfo.getLastModified();
        this.diskName = fileInfo.getDiskName();
    }

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
     *
     * @return type of the document
     */
    public DocumentType getDocumentType() {
        return documentType;
    }
    /**
     *
     * @param documentType type of the document, xlsx, pdf or dynamicform
     */
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
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
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate QR's creation date
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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


    /**
     * Appends new email to the edit list
     * Does not append if the email somehow exists in the list
     * @param toBeAddedEmail email
     */
    public void appendToE_info(String toBeAddedEmail){
        for(String email: this.e_info){
            if(email.equals(toBeAddedEmail)){
                // already exists somehow
                return;
            }
        }
        e_info.add(toBeAddedEmail);
    }

    /**
     * Appends new email to the view list
     * Does not append if the email somehow exists in the list
     * @param toBeAddedEmail
     */
    public void appendToV_info(String toBeAddedEmail){
        for(String email: this.v_info){
            if(email.equals(toBeAddedEmail)){
                // already exists somehow
                return;
            }
        }
        v_info.add(toBeAddedEmail);
    }


    /**
     * Add company email extension to the edit company list
     * does not append if the email extension already exists
     * @param companyEmailExtension
     */
    public void appendToE_company(String companyEmailExtension){
        for(String email: this.e_company){
            if(email.equals(companyEmailExtension)){
                // already exists somehow
                return;
            }
        }
        e_company.add(companyEmailExtension);
    }

    /**
     * Add company email extension to the view company list
     * does not append if the email extension already exists
     * @param companyEmailExtension
     */
    public void appendToV_company(String companyEmailExtension){
        for(String emailExtension: this.v_company){
            if(emailExtension.equals(companyEmailExtension)){
                // already exists
                return;
            }
        }
        v_company.add(companyEmailExtension);
    }


    /**
     * Finds user's right in this qr
     * @param userEmail email of the user
     */
    public RightType findUsersRightInQR(String userEmail){
        if(o_info.equals(userEmail)){
            return RightType.OWNER;
        }

        for(String email: e_info){
            if(email.equals(userEmail)){
                return RightType.EDIT;
            }
        }
        for(String email: v_info){
            if(email.equals(userEmail)){
                return RightType.VIEW;
            }
        }

        String emailExtension = userEmail.substring(userEmail.lastIndexOf("@") + 1);
        for(String companyMailExtension: e_company){
            if(companyMailExtension.equals(emailExtension)){
                return RightType.EDIT;
            }
        }
        for(String companyMailExtension: v_company){
            if(companyMailExtension.equals(emailExtension)){
                return RightType.VIEW;
            }
        }
        if(isPublic){
                return RightType.PUBLIC;
        }
        return RightType.NONE;
    }


}
