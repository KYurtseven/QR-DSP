package com.qrsynergy.service;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.UserDocument;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.repository.QRRepository;
import com.qrsynergy.repository.UserQRRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class QRService {

    @Autowired
    QRRepository qrRepository;
    @Autowired
    UserQRRepository userQRRepository;


    public void saveNewDocument(QR qr){
        // Save qr to the database
        qrRepository.save(qr);

        // save to the users
        addToUserQR(qr, qr.getV_info(), "view");
        addToUserQR(qr, qr.getE_info(), "edit");

        addQRToOwnerUserQR(qr);


    }

    private void addQRToOwnerUserQR(QR qr){
        // Save to the owner
        UserDocument userDocument = new UserDocument();
        userDocument.setName(qr.getOriginalName());
        userDocument.setUrl(qr.getUrl());
        UserQR userQR = userQRRepository.findByO_info(qr.getO_info());
        userQR.appendToO_docs(userDocument);

        userQRRepository.save(userQR);
    }

    private void addToUserQR(QR qr, List<String> emails, String type){

        // TODO
        // If the email is owner's email
        // Don't add

        for (String email: emails) {

            // Find userqr object
            UserQR userQR = userQRRepository.findByO_info(email);

            // create new userdocument to be added to the list
            UserDocument userDocument = new UserDocument();
            userDocument.setName(qr.getOriginalName());
            userDocument.setUrl(qr.getUrl());

            if(userQR == null){
                // create new user qr
                UserQR newUserQR = new UserQR();
                newUserQR.setO_info(email);

                if(type.equals("view")){
                    newUserQR.appendToV_docs(userDocument);
                }
                else{
                    newUserQR.appendToE_docs(userDocument);
                }
                userQRRepository.save(newUserQR);
            }
            else{
                // There exists userqr object
                if(type.equals("view")){
                    userQR.appendToV_docs(userDocument);
                }
                else{
                    userQR.appendToE_docs(userDocument);
                }
                userQRRepository.save(userQR);
            }

        }
    }

}
