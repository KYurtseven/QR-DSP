package com.qrsynergy.service;

import com.qrsynergy.model.Comment;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.UserDocument;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.repository.CommentRepository;
import com.qrsynergy.repository.QRRepository;
import com.qrsynergy.repository.UserQRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QRService {

    @Autowired
    QRRepository qrRepository;
    @Autowired
    UserQRRepository userQRRepository;
    @Autowired
    CommentRepository commentRepository;

    /**
     * If the isPublished field of the qr is true
     * publish the qr. Add qr to user's userqr document
     * In case of user does not exist in the database, create a userqr
     * in addToUserQR method
     * @param qr
     */
    public void publishQR(QR qr){
        // save to the users
        addToUserQR(qr, qr.getV_info(), "view");
        addToUserQR(qr, qr.getE_info(), "edit");
    }

    /**
     * Saves new QR to the database
     * If the publish field is false, meaning that do not publish it now,
     * don't add this QR to user's userqr document.
     * @param qr qr
     */
    public void saveNewDocument(QR qr){
        // Save qr to the database
        qrRepository.save(qr);

        // always save it to the owner's userqr
        addQRToOwnerUserQR(qr);

        // Create Comment entry for QR
        Comment comment = new Comment(qr.getUrl());
        commentRepository.save(comment);

        if(qr.getPublished()){
            publishQR(qr);
        }


        // else part
        // don't add it to the user's userqr
    }

    /**
     * Add this qr to owner's userqr document
     * @param qr qr
     */
    private void addQRToOwnerUserQR(QR qr){
        // Save to the owner
        UserDocument userDocument = new UserDocument();
        userDocument.setName(qr.getOriginalName());
        userDocument.setUrl(qr.getUrl());
        UserQR userQR = userQRRepository.findByO_info(qr.getO_info());
        userQR.appendToO_docs(userDocument);

        userQRRepository.save(userQR);
    }

    /**
     * Adds qr to user's userqr document
     * If the user does not exist, create a userqr document for him.
     * @param qr qr
     * @param emails list of emails
     * @param type 'edit' or 'view'
     */
    private void addToUserQR(QR qr, List<String> emails, String type){

        for (String email: emails) {
            // If the email is owner's email
            // Don't add
            if(email.equals(qr.getO_info())){
                continue;
            }
            // Find UserQR object
            UserQR userQR = userQRRepository.findByO_info(email);

            // create new UserDocument to be added to the list
            UserDocument userDocument = new UserDocument();
            userDocument.setName(qr.getOriginalName());
            userDocument.setUrl(qr.getUrl());

            // There is no entry in the database for that user
            // The user does not exist
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
                // There exists UserQR object
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


    public List<QR> findQRListByUrls(List<String> urls){
        return qrRepository.findByUrlIn(urls);
    }
}
