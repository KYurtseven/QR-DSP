package com.qrsynergy.controller;

import com.qrsynergy.controller.helper.ControllerResponse;
import com.qrsynergy.controller.helper.UserDTO;
import com.qrsynergy.model.Company;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.model.helper.Password;
import com.qrsynergy.model.User;
import com.qrsynergy.service.CompanyService;
import com.qrsynergy.service.UserQRService;
import com.qrsynergy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
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
     *
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
    public ControllerResponse signup(@Valid @RequestBody UserDTO userDTO) {

        // TODO
        // Do better approach
        ControllerResponse controllerResponse = new ControllerResponse();
        try{
            // Try to find out whether the user entered an unregistered company
            Company company = companyService.findByCompanyName(userDTO.getCompany());
            if(company == null){
                userDTO.setCompany(null);
            }
            // Check fields
            if(!isOkForSignUp(userDTO)){
                // don't add the user
                controllerResponse.setBody("Fields are required");
                return controllerResponse;
            }
            else{
                // don't accept if there is a user with that email
                User oldUser = userService.findByEmail(userDTO.getEmail());
                if(oldUser != null){
                    // There is already a registered user with that email
                    controllerResponse.setBody("Email is in use");
                    return controllerResponse;
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
            // create a user
            User user = new User(userDTO);
            // save the user to the database
            userService.saveUser(user);

            controllerResponse.setBody("User is saved");
            return controllerResponse;

        }
        catch(Exception e){
            controllerResponse.setBody("Error  "+ e);
            return controllerResponse;
        }
    }

    /**
     * Correct input
     * {
     * 	"email" : "koray.can.yurtseven@gmail.com",
     * 	"password" : "123123abc"
     * }
     * @param userDTO user credentials
     * @return success or failure
     */
    @PostMapping("/login")
    public ControllerResponse login(@Valid @RequestBody UserDTO userDTO){
        // TODO
        // Do better approach
        ControllerResponse controllerResponse = new ControllerResponse();

        try{
            User user = userService.findByEmail(userDTO.getEmail());
            if(user != null){
                byte[] salt = Password.stringToByte(user.getSalt());
                byte[] userPasswordInByte =  Password.hashPassword(userDTO.getPassword().toCharArray(), salt);

                String userHashedPassword = Password.bytetoString(userPasswordInByte);
                String dbPassword = user.getPassword();

                // TODO
                if(dbPassword.equals(userHashedPassword)){
                    controllerResponse.setBody("correct");
                }
                else{
                    controllerResponse.setBody("Incorrect email or password");
                }
                return controllerResponse;
            }
            else{
                controllerResponse.setBody("Incorrect email or password");
                return controllerResponse;
            }
        }
        catch(Exception e){
            controllerResponse.setBody("Error  "+ e);
            return controllerResponse;
        }
    }

}

