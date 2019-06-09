package com.qrsynergy.controller.helper;

public class CommentDTO {

    // url of the document
    private String url;

    // email of the user
    private String email;

    // body of the message
    private String message;

    /**
     * Empty constructor
     */
    public CommentDTO(){

    }

    /**
     * Constructor
     * @param url url of the document
     * @param email email of the user
     * @param message message to be added, the comment
     */
    public CommentDTO(String url, String email, String message){
        this.url = url;
        this.email = email;
        this.message = message;
    }

    /**
     *
     * @return url of the document
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url url to be set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return email of the owner
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email email to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return the comment
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message message to be set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
