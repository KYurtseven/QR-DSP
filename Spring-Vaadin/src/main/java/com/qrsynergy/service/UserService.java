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

    private final String INDIVIDUAL = "INDIVIDUAL";


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
            if(!checkEmailNamePassword(userDTO)){
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
            // Check company field
            if(userDTO.getCompany().length() == 0){
                signUpResponse.failureSignUpResponse(FailureMessage.COMPANY_FIELD_IS_EMPTY);
                return signUpResponse;
            }

            User user;
            if(userDTO.getCompany().toUpperCase().equals(INDIVIDUAL)){
                // Create a user object to be saved to the database
                user = new User(userDTO);
            }
            else{
                // check user's input in company field
                user = checkCompanySignUp(userDTO);
            }

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
     *
     * Checks validity of the user email
     * Checks length of the name
     * Checks length of the password
     *
     * if the user did not entered required fields
     * don't register the user
     * @param userDTO request of the user
     * @return true if fields are valid
     */
    private boolean checkEmailNamePassword(UserDTO userDTO){

        try{
            if(EmailValidator.getInstance().isValid(userDTO.getEmail())){
                if(userDTO.getFullName().length() < 3 ||
                        userDTO.getPassword().length() < 5){
                    return false;
                }
            }
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    /**
     * At this point, userDTO's email, password and name fields are correct
     * userDTO has a company name.
     *
     * Create a user.
     * Check validity of the company.
     *
     * Case 1 : Company is valid.
     * Case 1a: Company's email extension is equal to the user's email extension.
     *  Only valid case. ACCEPT
     * Case 1b: Email extensions are not equal. i.e. user: @ford.com, company:NISSAN
     *  REJECT
     *
     * Case 2: Company is not valid. i.e. company: FORT, N1is22an etc.
     *  REJECT
     *
     * @param userDTO user input
     * @return user a valid user input if company is correct, null if there is a problem
     */
    private User checkCompanySignUp(UserDTO userDTO){
        // create a user
        User user = new User(userDTO);

        // Try to find out whether the user entered an unregistered company
        Company company = companyService.findByCompanyName(userDTO.getCompany());
        // If the entered company name is not registered in our database
        // reject, case 2
        if(company == null){
            return null;
        }
        else{
            // If the company is registered and its email extension is not equal to
            // the user's email
            // (i.e. user: koray@ford.com, company mail extension is not: ford.com )
            // reject, case 1b
            if(!company.getEmailExtension().equals(user.getEmailExtension())){
                return null;
            }
            // accept, case 1a
            return user;
        }
    }
}
