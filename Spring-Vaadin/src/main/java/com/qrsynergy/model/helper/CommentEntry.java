package com.qrsynergy.model.helper;

import java.util.Date;

public class CommentEntry {

    private Date date;

    private String sender;

    private String message;

    /**
     * Constructor
     * @param sender email of the sender
     * @param date current date
     * @param message body of the message
     */
    public CommentEntry(String sender, Date date, String message){
        this.sender = sender;
        this.date = date;
        this.message = message;
    }
    /**
     * date of the comment
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * set date of the comment
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sender of the comment, email of the sender
     * @return
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets email of the sender
     * @param sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * body of the message
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * body of the message
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
