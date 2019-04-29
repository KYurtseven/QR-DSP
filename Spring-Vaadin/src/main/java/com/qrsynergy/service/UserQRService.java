package com.qrsynergy.service;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.model.helper.UserDocument;
import com.qrsynergy.repository.UserQRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserQRService {

    @Autowired
    UserQRRepository userQRRepository;

    /**
     * Finds user's UserQR by user's email
     * @param user
     * @return UserQR of the user
     */
    public UserQR getUserQR(User user){
        return userQRRepository.findByO_info(user.getEmail());
    }

    /**
     * Finds UserQR by email
     * @param email email of the user
     * @return UserQR of the user
     */
    public UserQR getUserQrByEmail(String email){
        return userQRRepository.findByO_info(email);
    }

    /**
     * Saves UserQR to the database
     * @param userQR
     */
    public void saveUserQR(UserQR userQR){
        userQRRepository.save(userQR);
    }

    /**
     * Removes QR from the user qr, with given rights
     * @param rightType
     * @param qr
     */
    public void removeQRFromUserQR(RightType rightType, QR qr){

        // Find userQR from the database
        UserQR userQR = userQRRepository.findByO_info(qr.getO_info());
        if(rightType.equals(RightType.EDIT)){
            // find correct UserDocument entry to be removed
            for(UserDocument userDocument: userQR.getE_docs()){
                if(userDocument.getUrl().equals(qr.getUrl())){
                    // remove from userQR
                    userQR.getE_docs().remove(userDocument);
                    // save to the database
                    userQRRepository.save(userQR);
                }
            }
        }
        else if(rightType.equals(RightType.VIEW)){
            // find correct UserDocument entry to be removed
            for(UserDocument userDocument: userQR.getV_docs()){
                if(userDocument.getUrl().equals(qr.getUrl())){
                    // remove from userQR
                    userQR.getV_docs().remove(userDocument);
                    // save to the database
                    userQRRepository.save(userQR);
                }
            }
        }
        else{
            // TODO
            // Remove from owner
        }
    }

    // TODO

    /**
     * Find userQR
     * Remove user document in the view list
     * Add user document to the edit list
     * i.e. Right improvements from view to edit
     */
    public void removeQRFromViewAndAddToEdit(String ownerEmail, QR qr){
        UserQR userQR = userQRRepository.findByO_info(ownerEmail);
        UserDocument temp;
        for(UserDocument userDocument: userQR.getV_docs()){
            if(userDocument.getUrl().equals(qr.getUrl())){
                // Store it in temp, to be added to edit list
                temp = userDocument;
                // remove from this array
                userQR.getE_docs().remove(userDocument);
                // add UserDocument to edit list
                userQR.appendToE_docs(temp);
                // save to the database
                userQRRepository.save(userQR);
                // break the for loop
                return;
            }
        }
    }
}
