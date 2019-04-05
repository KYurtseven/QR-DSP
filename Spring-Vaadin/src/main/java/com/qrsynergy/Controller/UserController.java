package com.qrsynergy.Controller;

import com.qrsynergy.model.Company;
import com.qrsynergy.model.Password;
import com.qrsynergy.model.User;
import com.qrsynergy.model.UserDocument;
import com.qrsynergy.service.CompanyService;
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
            Company company = companyService.findByCompanyName(userDTO.getCompany());
            if(company == null){
                userDTO.setCompany(null);
            }
            User user = new User(userDTO);

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

            byte[] salt = Password.stringToByte(user.getSalt());
            byte[] userPasswordInByte =  Password.hashPassword(userDTO.getPassword().toCharArray(), salt);

            String userHashedPassword = Password.bytetoString(userPasswordInByte);
            String dbPassword = user.getPassword();

            // TODO
            if(dbPassword.equals(userHashedPassword)){
                controllerResponse.setBody("correct");
            }
            else{
                controllerResponse.setBody("false");
            }
            return controllerResponse;
        }
        catch(Exception e){
            controllerResponse.setBody("Error  "+ e);
            return controllerResponse;
        }
    }
}

