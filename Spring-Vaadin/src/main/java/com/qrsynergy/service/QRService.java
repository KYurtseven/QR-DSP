package com.qrsynergy.service;

import com.qrsynergy.model.Comment;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.model.helper.UserDocument;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.repository.CommentRepository;
import com.qrsynergy.repository.QRRepository;
import com.qrsynergy.repository.UserQRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * in addQRToUserQR method
     * @param qr
     */
    public void publishQR(QR qr){
        // save to the users
        addQRToUserQR(qr, qr.getV_info(), RightType.VIEW);
        addQRToUserQR(qr, qr.getE_info(), RightType.EDIT);
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
     * @param rightType 'edit' or 'view'
     */
    private void addQRToUserQR(QR qr, List<String> emails, RightType rightType){

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
                if(rightType.equals(RightType.EDIT)){
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
                if(rightType.equals(RightType.VIEW)){
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
     *
     * Don't use this function with mixed urls.
     * i.e. don't mix view, own and edit urls in a list and try to fetch together.
     * If you do that, this functions return all of them and you must iterate all QR list
     * to find user's rights for each document
     * @param urls list of uuid of the documents
     * @return list of qr documents
     */
    @Deprecated
    public List<QR> findQRListByUrls(List<String> urls){
        return qrRepository.findByUrlIn(urls);
    }


    /**
     *
     * @param qr QR
     * @param toBeAddedEmail to be added email
     * @param rightType view or edit
     * @return true if successful, false on error
     */
    public boolean addUserToQR(QR qr, String toBeAddedEmail, RightType rightType){
        try{
            // Possible cases

            // Edit cases
            // Case 1: User is in edit list and is tried to be added edit, ignore
            // Case 2: User is in edit list and is tried to be added view, ignore

            // View cases
            // Case 3: User is in view list and is tried to be added view, ignore

            // Right improvements, from view to edit
            // Case 4: User is in view list and is tried to be added into edit list.
            //          Remove user from view, add to edit list. Save the document.
            //          Update userQR of the user

            // Case 5: User is freshly added to edit, accept. Create UserQR of the user
            //          if it is not present, add the new document. O.w. update UserQR of the user

            // Case 6: Same as above but with view rights.

            // Case 7: Ignore other cases
            if(rightType.equals(RightType.EDIT)){
                for(String email: qr.getE_info()){
                    if(email.equals(toBeAddedEmail)){
                        // Case 1
                        return false;
                    }
                }

                for(String email: qr.getV_info()){
                    if(email.equals(toBeAddedEmail)){
                        // Case 4
                        // remove from view, add to edit
                        qr.getV_info().remove(email);
                        qr.getE_info().add(email);
                        // Update UserQR
                        userQRService.removeQRFromViewAndAddToEdit(email, qr);
                        // save qr
                        qrRepository.save(qr);
                        return true;
                    }
                }
                // user is not in this list before

                // Case 5
                return(addUserToQRFreshUser(qr, toBeAddedEmail, RightType.EDIT));
            }
            if(rightType.equals(RightType.VIEW)){
                for(String email: qr.getE_info()){
                    if(email.equals(toBeAddedEmail)){
                        // Case 2
                        return false;
                    }
                }
                for(String email: qr.getV_info()){
                    if(email.equals(toBeAddedEmail)){
                        // Case 3
                        return false;
                    }
                }
                // Case 6
                return(addUserToQRFreshUser(qr, toBeAddedEmail, RightType.VIEW));
            }
            // Case 7
            return false;
        }
        catch(Exception e){
            System.out.println("Error in QRService: addUserToQR: "  + e);
            return false;
        }
    }

    /**
     * Helper method for addUserToQR, with case 5 and 6
     * toBeAddedEmail is definetly not in the QR.
     * Prepare a list of strings, in our case only 1 string which is the email of the user
     * use addQRToUserQR method to add this QR to the user's UserQR object.
     *
     * Then, save the qr with the new user
     *
     * @param qr
     * @param toBeAddedEmail
     * @param rightType
     * @return
     */
    private boolean addUserToQRFreshUser(QR qr, String toBeAddedEmail, RightType rightType){
        try{
            // Prepare a list for using the same function
            List<String> userEmail = new ArrayList<>();
            userEmail.add(toBeAddedEmail);
            // Add to userQR, create if the UserQR is not present
            // saves to the database
            addQRToUserQR(qr, userEmail, rightType);
            if(rightType.equals(RightType.EDIT)){
                qr.appendToE_info(toBeAddedEmail);
            }
            else{
                qr.appendToV_info(toBeAddedEmail);
            }
            qrRepository.save(qr);
            return true;
        }
        catch(Exception e){
            System.out.println("Exception in addUserToQRFreshUser: "  + e);
            return false;
        }
    }

    /**
     * Removes current user from QR
     * @param qr
     * @param toBeRemovedEmail
     * @return true if successful, false on error or owner removal
     */
    public boolean removeUserFromQR(QR qr, String toBeRemovedEmail, RightType rightType){

        // TODO
        // Remove from toBeRemovedEmail's UserQR
        try{
            if(rightType.equals(RightType.OWNER)){
                // TODO
                // cannot remove owner from the document
                // pass ownership to another one
                return false;
            }
            else if(rightType.equals(RightType.EDIT)){
                // find email in the edit people
                for(String email: qr.getE_info()) {
                    if (email.equals(toBeRemovedEmail)) {
                        qr.getE_info().remove(email);
                        break;
                    }
                }
                qrRepository.save(qr);
                return true;
            }
            else{
                // View
                for(String email: qr.getV_info()) {
                    if (email.equals(toBeRemovedEmail)) {
                        qr.getE_info().remove(email);
                        break;
                    }
                }
                qrRepository.save(qr);
                return true;
            }
        }
        catch(Exception e){
            // error
            System.out.println("Error in qr service, remove user: " + e);
            return false;
        }
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
