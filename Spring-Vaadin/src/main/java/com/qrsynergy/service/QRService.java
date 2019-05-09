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

import javax.jws.soap.SOAPBinding;
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
     * Creates a UserQR object to be saved to the database
     * adds first user document to the UserQR with given rights
     * @param email email of the user
     * @param userDocument first document to be added
     * @param rightType edit or view
     * @return
     */
    private UserQR createUserQRToBeAdded(String email, UserDocument userDocument, RightType rightType){
        UserQR newUserQR = new UserQR(email);
        // append UserDocument to appropriate list
        if(rightType.equals(RightType.EDIT)){
            newUserQR.appendToE_docs(userDocument);
        }
        else{
            newUserQR.appendToV_docs(userDocument);
        }
        return newUserQR;
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
                UserQR newUserQR = createUserQRToBeAdded(email, userDocument, rightType);
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
     * Add a new user to the QR, default view right
     * @Case1 : User is in the lists, don't accept
     * @Case2 : User is freshly added
     * @param qr QR
     * @param toBeAddedEmail to be added email
     * @return true if successful, false on error
     */
    public boolean addUserToQR(QR qr, String toBeAddedEmail){
        try{
            // Case 1
            for(String email: qr.getE_info()){
                if(email.equals(toBeAddedEmail)){
                    return false;
                }
            }
            for(String email: qr.getV_info()){
                if(email.equals(toBeAddedEmail)){
                    return false;
                }
            }
            // Case 2
            // Accept it. Check whether he has a userQR or not.
            // Create a userQR if he does not have any.
            // Add to the qr
            // Save

            // Add new user to the QR
            qr.appendToV_info(toBeAddedEmail);
            // Finds UserQR of the user
            UserQR userQR = userQRService.getUserQrByEmail(toBeAddedEmail);
            // create UserDocument to be added to the UserQR
            UserDocument userDocument = new UserDocument(qr.getOriginalName(), qr.getUrl(), qr.getDocumentType());
            // if there is no UserQR, the user has not registered previously
            if(userQR == null){
                // create new UserQR for (to be created) user
                UserQR newUserQR = createUserQRToBeAdded(toBeAddedEmail, userDocument, RightType.VIEW);

                qrRepository.save(qr);
                userQRService.saveUserQR(newUserQR);
                return true;
            }
            else{
                userQR.appendToV_docs(userDocument);

                qrRepository.save(qr);
                userQRService.saveUserQR(userQR);
                return true;
            }
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
     * Helper method of removeUserFromQR
     * Removes user from the QR
     * @param qr QR
     * @param toBeRemovedEmail email of the user
     */
    private void removeFromQR(QR qr, String toBeRemovedEmail){
        boolean isInEdit = false;
        for(String email: qr.getE_info()){
            if(email.equals(toBeRemovedEmail)){
                qr.getE_info().remove(email);
                isInEdit = true;
                break;
            }
        }
        if(!isInEdit){
            // Remove from view list if it is not found
            // in the edit list
            for(String email: qr.getV_info()){
                if(email.equals(toBeRemovedEmail)){
                    qr.getV_info().remove(email);
                    break;
                }
            }
        }
    }

    /**
     * Helper method of removeUserFromQR
     * Removes QR from the user's UserQR
     * @param qr QR
     * @param userQR userQR of the user
     */
    private void removeFromUserQR(QR qr, UserQR userQR){
        boolean isInEdit = false;
        // Find qr and remove
        for(UserDocument userDocument: userQR.getE_docs()){
            if(userDocument.getUrl().equals(qr.getUrl())){
                userQR.getE_docs().remove(userDocument);
                isInEdit = true;
                break;
            }
        }
        if(!isInEdit){
            // Remove from view list if it is not found
            // in the edit list
            for(UserDocument userDocument: userQR.getV_docs()){
                if(userDocument.getUrl().equals(qr.getUrl())){
                    userQR.getV_docs().remove(userDocument);
                    break;
                }
            }
        }
    }


    /**
     * Removes user from the qr
     * and removes qr from the user's UserQR
     * Save updated values to the database
     * @param qr QR
     * @param toBeRemovedEmail email of the user
     * @return true on successful, false otherwise
     */
    public boolean removeUserFromQR(QR qr, String toBeRemovedEmail){
        // Find and remove the user
        try{
            removeFromQR(qr, toBeRemovedEmail);

            // At this point, user is removed from the list.
            // QR is ready to be saved
            // Find userQR of the User
            UserQR userQR = userQRService.getUserQrByEmail(toBeRemovedEmail);
            if(userQR != null){
                removeFromUserQR(qr, userQR);
                // save updated values
                qrRepository.save(qr);
                userQRService.saveUserQR(userQR);
                return true;
            }
            // There is no userQR, something is wrong
            return false;
        }
        catch(Exception e){
            return false;
        }
    }


    /**
     * Helper method of changeRightOfUser
     * Changes right of the user in the QR
     * @param qr QR
     * @param toBeChangedEmail email of the user
     * @param oldRight old right
     */
    private void changeRightInQR(QR qr, String toBeChangedEmail, RightType oldRight){

        if(oldRight.equals(RightType.VIEW)){
            // remove from view list
            for(String email: qr.getV_info()){
                if(email.equals(toBeChangedEmail)){
                    qr.getV_info().remove(email);
                    break;
                }
            }
            // add to edit list
            qr.appendToE_info(toBeChangedEmail);
        }
        else{
            // remove from edit list
            for(String email: qr.getE_info()){
                if(email.equals(toBeChangedEmail)){
                    qr.getE_info().remove(email);
                    break;
                }
            }
            // add to view list
            qr.appendToV_info(toBeChangedEmail);
        }
    }


    /**
     * Helper method of changeRightOfUser
     * Changes right of the user's UserQR
     * Removes qr from old list, adds to the new list
     * @param qr QR
     * @param userQR userQR
     * @param oldRight old right
     */
    private void changeRightInUserQR(QR qr, UserQR userQR, RightType oldRight){

        UserDocument toBeChangedUserDocument = null;
        if(oldRight.equals(RightType.VIEW)){
            // remove from view list
            for(UserDocument userDocument: userQR.getV_docs()){
                if(userDocument.getUrl().equals(qr.getUrl())){
                    toBeChangedUserDocument = userDocument;
                    userQR.getV_docs().remove(userDocument);
                    break;
                }
            }
            // add to edit list
            if(toBeChangedUserDocument != null){
                userQR.appendToE_docs(toBeChangedUserDocument);
            }
        }
        else{
            // remove from edit list
            for(UserDocument userDocument: userQR.getE_docs()){
                if(userDocument.getUrl().equals(qr.getUrl())){
                    toBeChangedUserDocument = userDocument;
                    userQR.getE_docs().remove(userDocument);
                    break;
                }
            }
            // add to view list
            if(toBeChangedUserDocument != null){
                userQR.appendToV_docs(toBeChangedUserDocument);
            }
        }
    }

    /**
     * Changes right of the user from view to edit, or edit to view
     * and updates user's UserQR
     * Save updated values to the database
     * @param qr QR
     * @param toBeChangedEmail email of the user
     * @param oldRight old right
     * @return true on successful, false otherwise
     */
    public boolean changeRightOfUser(QR qr, String toBeChangedEmail, RightType oldRight){
        try{
            // update QR
            changeRightInQR(qr, toBeChangedEmail, oldRight);
            // At this point, user is moved from edit to view, or view to edit
            // QR is ready to be saved
            // Find user's UserQR
            UserQR userQR = userQRService.getUserQrByEmail(toBeChangedEmail);
            if(userQR != null){
                changeRightInUserQR(qr, userQR, oldRight);
                qrRepository.save(qr);
                userQRService.saveUserQR(userQR);
                return true;
            }
            return false;
        }
        catch(Exception e){
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
