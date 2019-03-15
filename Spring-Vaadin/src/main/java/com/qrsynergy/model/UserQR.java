package com.qrsynergy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "USERQR")
public class UserQR {

    @Id
    // owner info
    private String o_info;

    // list of documents that user owns
    private List<UserDocument> o_docs;

    // list of documents that user can edit
    private List<UserDocument> e_docs;

    // list of documents that user can view
    private List<UserDocument> v_docs;



}
