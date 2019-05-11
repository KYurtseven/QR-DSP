package com.qrsynergy.model.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class CommentEntry {

    private Date date;

    private String sender;

    private String message;


    private static final String pattern_dd_MM_yyyy = "dd-MM-yyyy";

    private static final String pattern_HH_mm = "HH:mm";
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
     *
     * @return date in dd-MM-yyyy format
     */
    public String getDateInDDMMYYYY(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern_dd_MM_yyyy);
        String date = simpleDateFormat.format(this.date);
        return date;
    }


    /**
     *
     * @return time of the date
     */
    public String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern_HH_mm);
        String date = simpleDateFormat.format(this.date);
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
     *
     * @return sender in camel case
     */
    public String getSenderCamelCase(){
        return StringUtils.capitalize(sender);
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
