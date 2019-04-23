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
     * Checks fields in the userDTO
     * if the user did not entered required fields
     * don't register the user
     * @param userDTO request of the user
     * @return whether our system should accept this request or not
     */
    private boolean isOkForSignUp(UserDTO userDTO){
        if(userDTO.getEmail().length() == 0 ||
                userDTO.getFullName().length() == 0 ||
                userDTO.getPassword().length() == 0){
            return false;

        }
        return true;
    }

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

        SignUpResponse signUpResponse = new SignUpResponse();

        try{
            // Check fields
            if(!isOkForSignUp(userDTO)){
                // don't add the user
                signUpResponse.failureSignUpResponse(FailureMessage.SIGNUP_FIELDS_REQUIRED);
                return ResponseEntity.badRequest().body(signUpResponse);
            }
            else{
                // don't accept if there is a user with that email
                User oldUser = userService.findByEmail(userDTO.getEmail());
                if(oldUser != null){
                    // There is already a registered user with that email
                    signUpResponse.failureSignUpResponse(FailureMessage.SIGNUP_EMAIL_IN_USE);
                    return ResponseEntity.badRequest().body(signUpResponse);
                }
            }
            // at this point, user input is correct
            // and there is no other user with the same email
            // try to find UserQR. If there is no UserQR, create it
            UserQR userQR = userQRService.getUserQrByEmail(userDTO.getEmail());
            if(userQR == null){
                // create a UserQR for the user
                UserQR newUserQR = new UserQR(userDTO.getEmail());
                // save
                userQRService.saveUserQR(newUserQR);
            }
            // check user's input in company field
            User user = checkCompanySignUp(userDTO);

            // save the user to the database
            userService.saveUser(user);
            signUpResponse.successSignUpResponse();

            return ResponseEntity.ok(signUpResponse);

        }
        catch(Exception e){
            signUpResponse.failureSignUpResponse(FailureMessage.UNKNOWN_ERROR);
            System.out.println("Exception in userController signup: " + e);
            return ResponseEntity.badRequest().body(signUpResponse);
        }
    }

    /**
     * Checks company field of user's input.
     * Check DB, whether the company exists or not
     * Check user's email extension and company extension. If they don't match,
     * set user's company to null
     * @param userDTO
     * @return user
     */
    private User checkCompanySignUp(UserDTO userDTO){
        // create a user
        User user = new User(userDTO);

        // Try to find out whether the user entered an unregistered company
        Company company = companyService.findByCompanyName(userDTO.getCompany());
        // If the entered company name is not registered in our database
        // set company of the user null
        if(company == null){
            user.setCompany(null);
        }
        else{
            // If the company is registered and its email extension is not equal to
            // the user's email
            // (i.e. user: koray@ford.com, company mail extension is not: ford.com )
            // reject, set company of the user null
            if(!company.getEmailExtension().equals(user.getEmailExtension())){
                user.setCompany(null);
            }
            // accept
        }
        return user;
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

