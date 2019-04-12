package com.qrsynergy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Document(collection = "COMMENT")
public class Comment {

    private ObjectId _id;


    @Id
    @Indexed(unique = true)
    private String url;

    private List<CommentEntry> commentEntrys;

    /**
     * Constructor
     * initialize list of Comment for access
     */
    public Comment(){
        List<CommentEntry> commentEntrys = new ArrayList<>();
        this.commentEntrys = commentEntrys;
    }

    /**
     * Overloaded constructor with url
     * initialize list of Comment for access
     * @param url
     */
    public Comment(String url){
        this.url = url;
        List<CommentEntry> commentEntrys = new ArrayList<>();
        this.commentEntrys = commentEntrys;
    }


    /**
     * adds new comment entity to list
     * @param commentEntry
     */
    public void appendToCommentEntry(CommentEntry commentEntry){
        commentEntrys.add(commentEntry);
    }


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    /**
     * Same as QR's url
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set when QR is created
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * List of comment entries
     * @return
     */
    public List<CommentEntry> getCommentEntrys() {
        return commentEntrys;
    }

    /**
     * List of comment entries
     * @param commentEntrys
     */
    public void setCommentEntrys(List<CommentEntry> commentEntrys) {
        this.commentEntrys = commentEntrys;
    }
}
