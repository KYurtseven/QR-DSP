package com.qrsynergy.controller;

import com.qrsynergy.controller.helper.FailureMessage;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.helper.DocumentType;
import com.qrsynergy.service.QRService;
import com.qrsynergy.ui.view.sharedocument.UploadFileStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/api/qr")
public class QRController {

    @Autowired
    QRService qrService;

    /**
     * REST API: /api/qr/view/{url}/{email}
     * ex:
     * localhost:8080/api/qr/view/d5ed2019-8e0a-4a47-b568-663f9974fe89/koray.can.yurtseven@gmail.com
     *
     * No input parameter, since it is get request
     *
     * Returns:
     * Status code: 200
     * {
     *  csv file in the body. raw text, line by line
     * }
     *
     * @param url url of the document
     * @param email email of the user who wants to read the document
     * @return
     */
    @GetMapping("/view/{url}/{email}")
    public ResponseEntity viewQR(@PathVariable String url, @PathVariable String email){

        ResponseEntity responseEntity = null;
        try{
            // Fetch QR
            QR qr = qrService.findQRByUrl(url);
            if(qr == null){
                // qr is not found response
                responseEntity = new ResponseEntity(FailureMessage.QR_NOT_FOUND, HttpStatus.OK);
                return responseEntity;
            }
            if(checkAccessRights(qr, email)){
                if(qr.getDocumentType().equals(DocumentType.EXCEL)){
                    return getCSV(qr);
                }
                else{
                    // TODO
                    return null;
                }
            }
            else{
                // cannot access this document, not enough rights
                responseEntity = new ResponseEntity(FailureMessage.QR_CANNOT_SEE_DOCUMENT, HttpStatus.UNAUTHORIZED);
                return responseEntity;
            }

        }
        catch(Exception e){
            responseEntity = new ResponseEntity(FailureMessage.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

            System.out.println("Exception in QRController viewQR: " + e);
            return responseEntity;
        }
    }

    /**
     * Finds CSV file in the path, puts it in response entity
     * @param qr qr
     * @return response entity containing csv file
     * @throws Exception exception
     */
    private ResponseEntity getCSV(QR qr) throws Exception{
        // csv path
        String fullFileName = UploadFileStep.uploadLocation + qr.getUrl() + ".csv";
        String fileName = qr.getUrl() + ".csv";

        ResponseEntity responseEntity = null;
        File file = new File(fullFileName);

        if(file.exists()){
            InputStream inputStream = new FileInputStream(fullFileName);

            byte[] out = org.apache.commons.io.IOUtils.toByteArray(inputStream);
            HttpHeaders responseHeaders = new HttpHeaders();

            responseHeaders.add("content-disposition", "attachment; filename=" + fileName);
            responseHeaders.add("Content-Type", "text/csv");
            responseEntity = new ResponseEntity(out, responseHeaders, HttpStatus.OK);
            return responseEntity;
        }
        else{
           responseEntity = new ResponseEntity(FailureMessage.QR_FILE_NOT_FOUND, HttpStatus.OK);
            return responseEntity;
        }
    }


    /**
     * Returns true if the email can access this document
     * @param qr qr
     * @param email email of the user
     * @return true if the user can access this document
     */
    private boolean checkAccessRights(QR qr, String email){
        // check whether this is the owner
        if(qr.getO_info().equals(email)){
            // owner access
            return true;
        }
        for(String mail: qr.getE_info()){
            if(mail.equals(email)){
                // edit access
                return true;
            }
        }
        for(String mail: qr.getV_info()){
            if(mail.equals(email)){
                // view access
                return true;
            }
        }
        if(qr.getPublic()){
            // public access
            return true;
        }
        // You cannot see this document
        return false;
    }
}
