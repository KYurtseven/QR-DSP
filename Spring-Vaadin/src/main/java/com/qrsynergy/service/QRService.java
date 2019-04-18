package com.qrsynergy.service;

import com.qrsynergy.model.Comment;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.helper.UserDocument;
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
    UserQRService userQRService;

    @Autowired
    QRRepository qrRepository;

    @Autowired
    CommentService commentService;

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
        // always save it to the owner's UserQR
        addQRToOwnerUserQR(qr);
        // Create Comment entry for QR
        Comment comment = new Comment(qr.getUrl());
        commentService.saveComment(comment);
        // publish now or later?
        if(qr.getPublished()){
            publishQR(qr);
        }
        // else part
        // don't add it to the user's UserQR
        // when the QR is published later, QR will be added user's UserQRs
        // in a different method
    }

    /**
     * Add this qr to owner's userqr document
     * @param qr qr
     */
    private void addQRToOwnerUserQR(QR qr){
        // create UserDocument
        UserDocument userDocument = new UserDocument(qr.getOriginalName(), qr.getUrl(), qr.getDocumentType());
        // find user's UserQR
        UserQR userQR = userQRService.getUserQrByEmail(qr.getO_info());
        // append to the user's owned document
        userQR.appendToO_docs(userDocument);
        // save
        userQRService.saveUserQR(userQR);
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
            // Find user's UserQR
            UserQR userQR = userQRService.getUserQrByEmail(email);
            // create UserDocument
            UserDocument userDocument = new UserDocument(qr.getOriginalName(), qr.getUrl(), qr.getDocumentType());

            // There is no entry in the database for that user
            // The user does not exist
            if(userQR == null){
                // create new UserQR for (to be created) user
                UserQR newUserQR = new UserQR(email);
                // append UserDocument to appropriate list
                if(type.equals("view")){
                    newUserQR.appendToV_docs(userDocument);
                }
                else{
                    newUserQR.appendToE_docs(userDocument);
                }
                // save
                userQRService.saveUserQR(newUserQR);
            }
            else{
                // There exists UserQR for the user
                // append UserDocument to appropriate list
                if(type.equals("view")){
                    userQR.appendToV_docs(userDocument);
                }
                else{
                    userQR.appendToE_docs(userDocument);
                }
                // save
                userQRService.saveUserQR(userQR);
            }

        }
    }


    /**
     * TODO
     * We are not using this right now
     *
     * Don't use this function with mixed urls.
     * i.e. don't mix view, own and edit urls in a list and try to fetch together.
     * If you do that, this functions return all of them and you must iterate all QR list
     * to find user's rights for each document
     * @param urls list of uuid of the documents
     * @return list of qr documents
     */
    public List<QR> findQRListByUrls(List<String> urls){
        return qrRepository.findByUrlIn(urls);
    }


    /**
     * Finds QR by url
     * @param url url of the QR
     * @return QR
     */
    public QR findQRByUrl(String url){
        return qrRepository.findByUrl(url);
    }
}
