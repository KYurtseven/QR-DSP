package com.qrsynergy.ui.view.createdocument;

import java.util.Date;

/**
 * Class for sending data from upload file step to submit button
 *
 */
public class FirstStepInfo {

    private String url;

    private String type;

    private String originalName;

    private Date creationDate;

    private Date lastModified;

    private String diskName;

    private byte[] fileInBytes;

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
     *
     * @return QR's creation date, now
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate QR's creation date, now
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     *
     * @return QR's last modified date, now
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     *
     * @param lastModified QR's last modified date, now
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
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
     *
     * @return byte[] of the uploaded file
     */
    public byte[] getFileInBytes() {
        return fileInBytes;
    }


    /**
     * Used for writing input stream to byte[]
     * Then byte [] to file in the submit step
     * @param fileInBytes converted input stream to byte[]
     */
    public void setFileInBytes(byte[] fileInBytes) {
        this.fileInBytes = fileInBytes;
    }
}
