package com.qrsynergy.service;

import com.qrsynergy.controller.helper.FailureMessage;
import com.qrsynergy.controller.helper.SignUpResponse;
import com.qrsynergy.controller.helper.UserDTO;
import com.qrsynergy.model.Company;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.qrsynergy.model.User;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserQRService userQRService;
    @Autowired
    CompanyService companyService;

    /**
     * Save user to the database
     * @param user user
     */
    @Deprecated
    public void saveUser(User user){
        userRepository.save(user);
    }

    /**
     * Fetch user from the database by email
     * @param email email of the user
     * @return user
     */
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    /**
     *
     * @param userDTO
     * @return
     */
    public SignUpResponse saveUser(UserDTO userDTO){
        SignUpResponse signUpResponse = new SignUpResponse();
        try{
            // Check fields
            if(!isOkForSignUp(userDTO)){
                // don't add the user
                signUpResponse.failureSignUpResponse(FailureMessage.SIGNUP_FIELDS_REQUIRED);
                return signUpResponse;
            }
            else{
                // don't accept if there is a user with that email
                User oldUser = findByEmail(userDTO.getEmail());
                if(oldUser != null){
                    // There is already a registered user with that email
                    signUpResponse.failureSignUpResponse(FailureMessage.SIGNUP_EMAIL_IN_USE);
                    return signUpResponse;
                }

            }
            // check user's input in company field
            User user = checkCompanySignUp(userDTO);
            if(user != null){
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
                // save the user to the database
                userRepository.save(user);

                signUpResponse.successSignUpResponse();
                return signUpResponse;
            }
            else{
                // Error on company mail match
                signUpResponse.failureSignUpResponse(FailureMessage.EMAIL_COMPANYMAIL_DONT_MATCH);
                return signUpResponse;
            }
        }
        catch(Exception e){
            signUpResponse.failureSignUpResponse(FailureMessage.UNKNOWN_ERROR);
            System.out.println("Exception in userController signup: " + e);
            return signUpResponse;
        }
    }

    /**
     * Checks fields in the userDTO
     * if the user did not entered required fields
     * don't register the user
     * @param userDTO request of the user
     * @return whether our system should accept this request or not
     */
    private boolean isOkForSignUp(UserDTO userDTO){

        if(EmailValidator.getInstance().isValid(userDTO.getEmail())){
            if(userDTO.getEmail().length() == 0 ||
                    userDTO.getFullName().length() == 0 ||
                    userDTO.getPassword().length() == 0){
                return false;
            }
        }

        return true;
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
        // reject
        if(company == null){
            return null;
        }
        else{
            // If the company is registered and its email extension is not equal to
            // the user's email
            // (i.e. user: koray@ford.com, company mail extension is not: ford.com )
            // reject,
            if(!company.getEmailExtension().equals(user.getEmailExtension())){
                return null;
            }
            // accept
        }
        return user;
    }
}
