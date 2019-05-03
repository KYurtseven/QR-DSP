package com.qrsynergy.model.helper;

/**
 * Not a database table entity
 * UserQR has list of UserDocuments
 * {
 *    "name": "Visible original name of the file.xlsx",
 *    "url": "4da6b757-f33e-4ff2-bfbd-e78c557245f8"
 * }
 * Stores that kind of information to help UserQR object
 */
public class UserDocument {

    private String name;

    private String url;

    private DocumentType documentType;

    /**
     * Constructor
     * @param name original name of the document
     * @param url uuid of the document
     */
    public UserDocument(String name, String url, DocumentType documentType){
        this.name = name;
        this.url = url;
        this.documentType = documentType;
    }

    /**
     * ex: "23-24 subat egitmen semineri.xlsx"
     * @return visible name of the document
     */
    public String getName() {
        return name;
    }

    /**
     * ex: "23-24 subat egitmen semineri.xlsx"
     * @param name original name of the document
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * To find document in the database
     * @return url, unique id of the document
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets unique id of the document
     * @param url url, unique id of the document
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return document type
     */
    public DocumentType getDocumentType() {
        return documentType;
    }

    /**
     *
     * @param documentType document type to set
     */
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
}
