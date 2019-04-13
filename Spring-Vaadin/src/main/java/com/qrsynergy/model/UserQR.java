package com.qrsynergy.model;

import com.qrsynergy.model.helper.UserDocument;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Document(collection = "USERQR")
public class UserQR {

    private ObjectId _id;

    @Id
    @Indexed(unique = true)
    // owner info
    // email of the owner
    private String o_info;

    // list of documents that user owns
    private List<UserDocument> o_docs;

    // list of documents that user can edit
    private List<UserDocument> e_docs;

    // list of documents that user can view
    private List<UserDocument> v_docs;

    /**
     * Constructor
     * @param o_info owner's email
     */
    public UserQR(String o_info){
        this.o_info = o_info;
        initializeLists();
    }

    /**
     * initialize list of UserDocuments for access
     */
    private void initializeLists(){
        List<UserDocument> o_docs = new ArrayList<>();
        this.o_docs = o_docs;

        List<UserDocument> e_docs = new ArrayList<>();
        this.e_docs = e_docs;

        List<UserDocument> v_docs = new ArrayList<>();
        this.v_docs = v_docs;
    }

    public String getO_info() {
        return o_info;
    }

    public void setO_info(String o_info) {
        this.o_info = o_info;
    }

    public List<UserDocument> getO_docs() {
        return o_docs;
    }

    public void setO_docs(List<UserDocument> o_docs) {
        this.o_docs = o_docs;
    }

    public List<UserDocument> getE_docs() {
        return e_docs;
    }

    public void setE_docs(List<UserDocument> e_docs) {
        this.e_docs = e_docs;
    }

    public List<UserDocument> getV_docs() {
        return v_docs;
    }

    public void setV_docs(List<UserDocument> v_docs) {
        this.v_docs = v_docs;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public void appendToO_docs(UserDocument userDocument){
        o_docs.add(userDocument);
    }

    public void appendToV_docs(UserDocument userDocument){
        v_docs.add(userDocument);
    }

    public void appendToE_docs(UserDocument userDocument){
        e_docs.add(userDocument);
    }


}
