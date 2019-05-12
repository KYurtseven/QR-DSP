package com.qrsynergy.controller;

import com.qrsynergy.controller.helper.FailureMessage;
import com.qrsynergy.model.Company;
import com.qrsynergy.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CompanyController {

    @Autowired
    CompanyService companyService;


    /**
     * REST API: /api/company/listAll
     *
     * ex:
     * localhost:8080/api/company/listAll
     *
     * No input parameter
     *
     * TODO
     * Make a company response object and don't send id field at all
     * Returns :
     * Status code: 200
     * [
     *     {
     *         "_id": null,
     *         "name": "FORD",
     *         "emailExtension": "ford.com"
     *     },
     *     {
     *         "_id": null,
     *         "name": "NISSAN",
     *         "emailExtension": "nissan.com"
     *     },
     *     {
     *         "_id": null,
     *         "name": "OPEL",
     *         "emailExtension": "opel.com"
     *     }
     * ]
     * @return
     */
    @GetMapping("/listAll")
    public ResponseEntity listAll(){
        ResponseEntity responseEntity = null;
        try{
            List<Company> companyList = companyService.findAll();

            for(Company company: companyList){
                company.set_id(null);
            }
            responseEntity = new ResponseEntity(companyList, HttpStatus.OK);
            return responseEntity;
        }
        catch(Exception e){
            responseEntity = new ResponseEntity(FailureMessage.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        }
    }
}
