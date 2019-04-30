package com.qrsynergy.controller;

import com.qrsynergy.controller.helper.*;
import com.qrsynergy.model.Company;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.model.helper.Password;
import com.qrsynergy.model.User;
import com.qrsynergy.service.CompanyService;
import com.qrsynergy.service.UserQRService;
import com.qrsynergy.service.UserService;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    CompanyService companyService;
    @Autowired
    UserService userService;
    @Autowired
    UserQRService userQRService;



    /**
     * REST API: /api/user/signup
     * Correct input
     * {
     * 	"fullName" : "koray can yurtseven",
     * 	"email" : "koray.can.yurtseven@gmail.com",
     * 	"company" : "FORD",
     * 	"password" : "123123abc"
     * }
     *
     * @param userDTO user credentials
     * @return success or failure
     */
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody UserDTO userDTO) {

        SignUpResponse signUpResponse;

        try{
            signUpResponse = userService.saveUser(userDTO);
            if(signUpResponse.getStatus().equals(ResponseStatusType.FAILURE)){
                return ResponseEntity.badRequest().body(signUpResponse);
            }
            else {
                return ResponseEntity.ok(signUpResponse);
            }
        }
        catch(Exception e){
            signUpResponse =  new SignUpResponse();
            signUpResponse.failureSignUpResponse(FailureMessage.UNKNOWN_ERROR);
            return ResponseEntity.badRequest().body(signUpResponse);
        }

    }


    /**
     * REST API: /api/user/login
     * Correct input
     * {
     * 	"email" : "koray.can.yurtseven@gmail.com",
     * 	"password" : "123123abc"
     * }
     * Output success
     * Status code : 200
     * {
     *     "status" : "SUCCESS",
     *     "message" : null,
     *     "fullName" : "full name of the user,
     *     "email" : "email of the user",
     *     "company" : "company of the user"
     * }
     *
     * Output failure
     * Status code: 400
     * {
     *     "status" : "FAILURE,
     *     "message" : "failure message"
     * }
     *
     * @param userDTO user credentials
     * @return login response
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserDTO userDTO){

        LoginResponse loginResponse = new LoginResponse();

        try{
            User user = userService.findByEmail(userDTO.getEmail());
            if(user != null){
                byte[] salt = Password.stringToByte(user.getSalt());
                byte[] userPasswordInByte =  Password.hashPassword(userDTO.getPassword().toCharArray(), salt);

                String userHashedPassword = Password.bytetoString(userPasswordInByte);
                String dbPassword = user.getPassword();

                if(dbPassword.equals(userHashedPassword)){
                    loginResponse.successLoginResponse(user);
                    return ResponseEntity.ok(loginResponse);
                }
                else{
                    loginResponse.failureLoginResponse(FailureMessage.LOGIN_INCORRECT_CREDENTIALS);
                }

            }
            else{
                loginResponse.failureLoginResponse(FailureMessage.LOGIN_INCORRECT_CREDENTIALS);
            }
            return ResponseEntity.badRequest().body(loginResponse);
        }
        catch(Exception e){
            loginResponse.failureLoginResponse(FailureMessage.UNKNOWN_ERROR);
            System.out.println("Exception in userController login: " + e);
            return ResponseEntity.badRequest().body(loginResponse);
        }

    }

}

