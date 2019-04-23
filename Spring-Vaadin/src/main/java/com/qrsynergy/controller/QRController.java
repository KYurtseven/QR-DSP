package com.qrsynergy.controller;

import com.qrsynergy.controller.helper.FailureMessage;
import com.qrsynergy.controller.helper.QRAccessType;
import com.qrsynergy.controller.helper.QRResponse;
import com.qrsynergy.model.QR;
import com.qrsynergy.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QRController {

    @Autowired
    QRService qrService;

    /**
     *
     * @param url url of the document
     * @param email email of the user who wants to read the document
     * @return
     */
    @GetMapping("/view/{url}/{email}")
    public QRResponse viewQR(@PathVariable String url, @PathVariable String email){
        QRResponse qrResponse = new QRResponse();

        try{
            // Fetch QR
            QR qr = qrService.findQRByUrl(url);
            if(qr == null){
                // qr is not found response
                qrResponse.failureQRResponse(FailureMessage.QR_NOT_FOUND);
                return qrResponse;
            }
            if(checkAccessRights(qrResponse, qr, email)){
                // TODO
            }
            else{
                // cannot access this document, not enough rights
                qrResponse.failureQRResponse(FailureMessage.QR_CANNOT_SEE_DOCUMENT);
            }

        }
        catch(Exception e){
            qrResponse.failureQRResponse(FailureMessage.UNKNOWN_ERROR);
            System.out.println("Exception in QRController viewQR: " + e);
            return qrResponse;
        }
        return qrResponse;
    }

    /**
     * Returns true if the email can access this document
     * @param qrResponse response object which will be returned to the user
     * @param qr qr
     * @param email email of the user
     * @return true if the user can access this document
     */
    private boolean checkAccessRights(QRResponse qrResponse, QR qr, String email){
        // check whether this is the owner
        if(qr.getO_info().equals(email)){
            // owner access
            qrResponse.setAccessType(QRAccessType.OWNER);
            return true;
        }
        for(String mail: qr.getE_info()){
            if(mail.equals(email)){
                // edit access
                qrResponse.setAccessType(QRAccessType.EDIT);
                return true;
            }
        }
        for(String mail: qr.getV_info()){
            if(mail.equals(email)){
                // view access
                qrResponse.setAccessType(QRAccessType.VIEW);
                return true;
            }
        }
        if(qr.getPublic()){
            // public access
            qrResponse.setAccessType(QRAccessType.PUBLIC);
            return true;
        }
        // You cannot see this document
        return false;
    }
}
